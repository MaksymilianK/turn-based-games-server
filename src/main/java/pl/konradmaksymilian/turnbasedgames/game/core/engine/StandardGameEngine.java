package pl.konradmaksymilian.turnbasedgames.game.core.engine;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;
import pl.konradmaksymilian.turnbasedgames.game.core.action.SharedGameActionName;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.GameCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.GameSettingsChangeCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.PlayerTeamChangeCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.PlayerTeamShiftCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.action.event.GameEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.action.event.GameStartEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.action.event.PlayerEscapeEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.event.GameCountdownStartEventDto;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.event.GameSettingsChangeEventDto;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.event.GameStartEventDto;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.event.PlayerEscapeEventDto;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.event.PlayerTeamChangeEventDto;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.event.PlayerTeamShiftEventDto;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.player.SimplePlayer;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.player.StandardPlayerManager;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.timer.GameTimer;

public abstract class StandardGameEngine<T1 extends GameTimer, T2 extends StandardPlayerManager<? extends SimplePlayer, ?>>
		implements GameEngine {

	protected GameStatus status;
	
	protected final EventManager eventManager;
	
	protected final T1 timer;
	
	protected final T2 playerManager;
	
	public StandardGameEngine(EventManager eventManager, T1 timer, T2 playerManager) {
		this.playerManager = playerManager;
		this.timer = timer;
		this.eventManager = eventManager;
	}
	
	@Override
	public void processCommand(GameCommand command) {
		int code = command.getCode();
		GameEvent event = null;
				
		if (code == SharedGameActionName.GAME_COUNTDOWN_START.code()) {
			start();
		} else if (code == SharedGameActionName.GAME_SETTINGS_CHANGE.code()) {
			changeSettings((GameSettingsChangeCommand) command);
		} else if (code == SharedGameActionName.PLAYER_TEAM_CHANGE.code()) {
			changeTeam((PlayerTeamChangeCommand) command);
		} else if (code == SharedGameActionName.PLAYER_TEAM_SHIFT.code()) {
			shiftPlayerTeam((PlayerTeamShiftCommand) command);
		} else {
			if (status.equals(GameStatus.STARTED)) {
				throw new BadOperationException("Cannot send the command; the game has not started yet");
			} else if (command.getSenderId() != playerManager.getCurrentlyMovingUserId()) {
				throw new BadOperationException("Cannot send the command; it's not a turn of the current user");
			}
			
			event = processSpecificCommand(command);
		}
		
		if (event != null) {
			eventManager.store(event);
		}
	}

	@Override
	public GameStatus getStatus() {
		return status;
	}

	@Override
	public void addPlayer(int userId) {
		if (status.equals(GameStatus.NOT_STARTED)) {
			playerManager.addPlayer(userId);
		} else {
			throw new GameEngineException("Cannot add a player while a game is running already");
		}
	}

	@Override
	public int getMaxPlayers() {
		return playerManager.getMaxPlayers();
	}
	
	public void start() {
		throwExceptionIfStartedOrCountdown();
		if (playerManager.countPlayers() < playerManager.getMinPlayers()) {
			throw new BadOperationException("Cannot start a game when there is too few players");
		}

		status = GameStatus.COUNTDOWN;
		
		timer.countdown(() -> {
			var now = timer.getNow();
			eventManager.store(new GameStartEvent(now));
			eventManager.publish(new GameStartEventDto(now.toEpochMilli()));
			status = GameStatus.STARTED;
			onStart();
		});
		
		eventManager.publish(new GameCountdownStartEventDto(timer.getNow().toEpochMilli()));
		playerManager.start();
		initialize();
	}
	
	public void changeSettings(GameSettingsChangeCommand command) {
		if (!command.getNewSettings().getGame().equals(getGame())) {
			throw new GameEngineException("Cannot change game settings; invalid new settings");
		}
		throwExceptionIfStartedOrCountdown();
		
		changeGameSettings(command);
		eventManager.publish(new GameSettingsChangeEventDto(timer.getNowEpochMilli(), command.getNewSettings()));
	}
	
	private void changeTeam(PlayerTeamChangeCommand command) {
		throwExceptionIfStartedOrCountdown();
		
		playerManager.changeTeam(command.getSenderId(), command.getNewTeam());
		eventManager.publish(new PlayerTeamChangeEventDto(timer.getNowEpochMilli(), command.getSenderId(), 
				command.getNewTeam()));
	}
	
	private void shiftPlayerTeam(PlayerTeamShiftCommand command) {
		throwExceptionIfStartedOrCountdown();
		
		playerManager.shiftTeams(command.getFirstTeam(), command.getSecondTeam());
		eventManager.publish(new PlayerTeamShiftEventDto(timer.getNowEpochMilli(), command.getFirstTeam(), 
				command.getSecondTeam()));
	}
	
	@Override
	public int countPlayers() {
		return playerManager.countPlayers();
	}

	@Override
	public boolean containsPlayer(int userId) {
		return playerManager.containsPlayerByUserId(userId);
	}

	@Override
	public void removePlayer(int userId) {
		int team = playerManager.removePlayerByUserId(userId);
		if (!isNotStarted()) {
			cleanAfterPlayer(team);
			var now = timer.getNow();
			eventManager.store(new PlayerEscapeEvent(now, team));
			eventManager.publish(new PlayerEscapeEventDto(now.toEpochMilli(), team));
		}
	}
	
	@Override
	public Set<Integer> getPlayersUsersIds() {
		var players = new HashSet<Integer>();
		players.addAll(playerManager.getPlayers().values());
		return Collections.unmodifiableSet(players);
	}

	public T2 getPlayerManager() {
		return playerManager;
	}
	
	public T1 getTimer() {
		return timer;
	}
	
	public boolean isNotStarted() {
		return status.equals(GameStatus.NOT_STARTED);
	}
	
	@Override
	public void injectRoomId(int roomId) {
		eventManager.injectRoomId(roomId);
	}
	
	protected void cannotRecogniseCommand() {
		throw new BadOperationException("The command unrecognised");
	}
	
	protected void finish() {
		status = GameStatus.NOT_STARTED;
		timer.stop();
		eventManager.publishGameHistory();
		eventManager.reset();
	}
	
	private void throwExceptionIfStartedOrCountdown() {
		if (!isNotStarted()) {
			throw new BadOperationException("Cannot process the command while a game is already started");
		}
	}
		
	protected abstract GameEvent processSpecificCommand(GameCommand command);
	
	protected abstract void onStart();
	
	protected abstract void initialize();
		
	protected abstract void cleanAfterPlayer(int userId);
		
	protected abstract void changeGameSettings(GameSettingsChangeCommand command);
}
