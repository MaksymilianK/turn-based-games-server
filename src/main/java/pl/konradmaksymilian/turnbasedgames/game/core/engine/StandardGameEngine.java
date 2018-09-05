package pl.konradmaksymilian.turnbasedgames.game.core.engine;

import java.util.Set;

import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.GameCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.GameSettingsChangeCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.PlayerTeamChangeCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.PlayerTeamShiftCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.action.event.GameCountdownStartEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.action.event.GameEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.action.event.GameSettingsChangeEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.action.event.GameStartEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.action.event.PlayerTeamChangeEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.action.event.PlayerTeamShiftEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.SharedGameAction;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.player.PlayerManager;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.player.SimplePlayer;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.timer.GameTimer;

public abstract class StandardGameEngine<T1 extends GameTimer, T2 extends PlayerManager<? extends SimplePlayer>>
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
		GameEvent eventToPublish = null;
				
		if (code == SharedGameAction.GAME_COUNTDOWN_START.code()) {
			start();
		} else if (code == SharedGameAction.GAME_SETTINGS_CHANGE.code()) {
			changeSettings((GameSettingsChangeCommand) command);
		} else if (code == SharedGameAction.PLAYER_TEAM_CHANGE.code()) {
			changeTeam((PlayerTeamChangeCommand) command);
		} else if (code == SharedGameAction.PLAYER_TEAM_SHIFT.code()) {
			shiftPlayerTeam((PlayerTeamShiftCommand) command);
		} else {
			if (status.equals(GameStatus.STARTED)) {
				throw new BadOperationException("Cannot send the command; the game has not started yet");
			} else if (command.getSenderId() != playerManager.getCurrentlyMovingUserId()) {
				throw new BadOperationException("Cannot send the command; it's not a turn of the current user");
			}
			
			eventToPublish = processSpecificCommand(command);
		}
		
		if (eventToPublish != null) {
			eventManager.publishAndStore(eventToPublish);
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
	
	public GameStartEvent start() {
		throwExceptionIfStartedOrCountdown();
		if (playerManager.countPlayers() < playerManager.getMinPlayers()) {
			throw new BadOperationException("Cannot start a game when there is too few players");
		}

		status = GameStatus.COUNTDOWN;
		
		timer.countdown(() -> {
			eventManager.publishAndStore(new GameStartEvent());
			status = GameStatus.STARTED;
			onStart();
		});
		
		eventManager.publish(new GameCountdownStartEvent());
		playerManager.start();
		initialize();
		return null;
	}
	
	public GameSettingsChangeEvent changeSettings(GameSettingsChangeCommand command) {
		if (!command.getNewSettings().getGame().equals(getGame())) {
			throw new GameEngineException("Cannot change game settings; invalid new settings");
		}
		throwExceptionIfStartedOrCountdown();
		
		changeGameSettings(command);
		return new GameSettingsChangeEvent(command.getNewSettings());
	}
	
	private PlayerTeamChangeEvent changeTeam(PlayerTeamChangeCommand command) {
		throwExceptionIfStartedOrCountdown();
		
		playerManager.changeTeam(command.getSenderId(), command.getNewTeam());
		return new PlayerTeamChangeEvent(command.getSenderId(), command.getNewTeam());
	}
	
	private PlayerTeamShiftEvent shiftPlayerTeam(PlayerTeamShiftCommand command) {
		throwExceptionIfStartedOrCountdown();
		
		playerManager.shiftTeams(command.getFirstTeam(), command.getSecondTeam());
		return new PlayerTeamShiftEvent(command.getFirstTeam(), command.getSecondTeam());
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
		if (isNotStarted()) {
			playerManager.removePlayerByUserId(userId);
		} else {
			cleanAfterPlayer(userId);
		}
	}

	@Override
	public Set<Integer> getPlayersUsersIds() {
		return playerManager.getPlayersUsersIds();
	}
	
	public boolean isNotStarted() {
		return status.equals(GameStatus.NOT_STARTED);
	}
	
	private void throwExceptionIfStartedOrCountdown() {
		if (!isNotStarted()) {
			throw new BadOperationException("Cannot process the command while a game is already started");
		}
	}
	
	protected void cannotRecogniseCommand() {
		throw new BadOperationException("The command unrecognised");
	}
		
	protected abstract GameEvent processSpecificCommand(GameCommand command);
	
	protected abstract void onStart();
	
	protected abstract void initialize();
		
	protected abstract void cleanAfterPlayer(int userId);
		
	protected abstract void changeGameSettings(GameSettingsChangeCommand command);
}
