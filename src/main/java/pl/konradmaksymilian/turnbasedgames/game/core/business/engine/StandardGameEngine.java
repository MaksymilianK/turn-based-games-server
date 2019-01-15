package pl.konradmaksymilian.turnbasedgames.game.core.business.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.event.GameEventManager;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.player.StandardPlayerManager;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.timer.GameTimer;
import pl.konradmaksymilian.turnbasedgames.game.core.data.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.GameEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.GameStartEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.PlayerEscapeEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.PlayerGameEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.SharedGameEventName;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsChangeResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameStatusDetailsDto;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request.GameRequest;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request.GameSettingsChangeRequest;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request.PlayerTeamChangeRequest;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request.PlayerTeamShiftRequest;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response.GameCountdownStartResponse;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response.GameSettingsChangeResponse;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response.GameStartResponse;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response.PlayerEscapeResponse;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response.PlayerTeamChangeResponse;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response.PlayerTeamShiftResponse;
import pl.konradmaksymilian.turnbasedgames.room.business.RoomMessageSender;

public abstract class StandardGameEngine<T1 extends GameTimer, 
		T2 extends StandardPlayerManager<?, ?>> implements GameEngine {

	protected GameStatus status;
	protected final GameEventManager eventManager;
	protected final T1 timer;
	protected final T2 playerManager;
	
	public StandardGameEngine(GameEventManager eventManager, T1 timer, T2 playerManager) {
		this.eventManager = eventManager;
		this.timer = timer;
		this.playerManager = playerManager;
	}
	
	@Override
	public void processMessage(GameRequest message) {
		int code = message.getCode();
		GameEvent event = null;
				
		if (code == SharedGameEventName.GAME_COUNTDOWN_START.code()) {
			start();
		} else if (code == SharedGameEventName.GAME_SETTINGS_CHANGE.code()) {
			changeSettings((GameSettingsChangeRequest) message);
		} else if (code == SharedGameEventName.PLAYER_TEAM_CHANGE.code()) {
			changeTeam((PlayerTeamChangeRequest) message);
		} else if (code == SharedGameEventName.PLAYER_TEAM_SHIFT.code()) {
			shiftPlayerTeam((PlayerTeamShiftRequest) message);
		} else {
			if (status.equals(GameStatus.STARTED)) {
				throw new BadOperationException("Cannot send the message; the game has not started yet");
			} else if (message.getSender().getNick() != playerManager.getCurrentlyMovingUserNick()) {
				throw new BadOperationException("Cannot send the message; it's not a turn of the current user");
			}
			event = processSpecificMessage(message);
		}
		
		if (event != null) {
			eventManager.store(event);
		}
	}
	
	@Override
	public void injectMessageSender(RoomMessageSender messageSender) {
		this.eventManager.injectMessageSender(messageSender);
	}

	@Override
	public Collection<String> getPlayers() {
		return playerManager.getPlayers().values();
	}
	
	@Override
	public GameStatus getStatus() {
		return status;
	}

	@Override
	public void addPlayer(String nick) {
		if (status.equals(GameStatus.NOT_STARTED)) {
			playerManager.addPlayer(nick);
		} else {
			throw new GameEngineException("Cannot add a player while a game is running already");
		}
	}

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
			playerManager.start();
			var now = timer.getNow();
			eventManager.store(new GameStartEvent(now));
			eventManager.publish(new GameStartResponse(now.toEpochMilli()));
			status = GameStatus.STARTED;
			onStart();
		});
		
		eventManager.publish(new GameCountdownStartResponse(timer.getNow().toEpochMilli()));
		initialize();
	}
	
	public void changeSettings(GameSettingsChangeRequest message) {
		if (!message.getNewSettings().getGame().equals(getGame())) {
			throw new GameEngineException("Cannot change game settings; invalid new settings");
		}
		throwExceptionIfStartedOrCountdown();
		
		var newSettingsResponse = changeGameSettings(message);
		eventManager.publish(new GameSettingsChangeResponse(newSettingsResponse, timer.getNowEpochMilli()));
	}
	
	private void changeTeam(PlayerTeamChangeRequest message) {
		throwExceptionIfStartedOrCountdown();
		
		playerManager.changeTeam(message.getSender().getNick(), message.getNewTeam());
		eventManager.publish(new PlayerTeamChangeResponse(message.getSender().getId(), message.getNewTeam(), 
				timer.getNowEpochMilli()));
	}
	
	private void shiftPlayerTeam(PlayerTeamShiftRequest message) {
		throwExceptionIfStartedOrCountdown();
		
		playerManager.shiftTeams(message.getFirstTeam(), message.getSecondTeam());
		eventManager.publish(new PlayerTeamShiftResponse(message.getFirstTeam(), message.getSecondTeam(),
				timer.getNowEpochMilli()));
	}

	@Override
	public void removePlayer(String nick) {
		int team = playerManager.removePlayerByUserNick(nick);
		if (!isNotStarted()) {
			cleanAfterPlayer(team);
			var now = timer.getNow();
			eventManager.store(new PlayerEscapeEvent(now, team));
			eventManager.publish(new PlayerEscapeResponse(team, now.toEpochMilli()));
		}
	}
	
	public Set<String> getPlayersUsersNicks() {
		var players = new HashSet<String>();
		players.addAll(playerManager.getPlayers().values());
		return Collections.unmodifiableSet(players);
	}
	
	public boolean isNotStarted() {
		return status.equals(GameStatus.NOT_STARTED);
	}
	
	public List<GameEvent> getCurrentlyMovingPlayerLastEvents() {
		var lastPlayerEvents = new ArrayList<GameEvent>();
		
		for (int i = 0; i < 6; i++) {
			eventManager.findFromTail(i).ifPresent(event -> {
				if (((PlayerGameEvent) event).getTeam() == this.playerManager.getCurrentlyMovingTeam()) {
					lastPlayerEvents.add(event);
				}
			});
		}
		return lastPlayerEvents;
	}
	
	protected void cannotRecogniseRequest() {
		throw new BadOperationException("The message unrecognised");
	}
	
	protected void finish() {
		status = GameStatus.NOT_STARTED;
		timer.stop();
		eventManager.reset();
	}
	
	private void throwExceptionIfStartedOrCountdown() {
		if (!isNotStarted()) {
			throw new BadOperationException("Cannot process the message while a game is already started");
		}
	}
	
	public abstract Game getGame();
	protected abstract GameStatusDetailsDto getStatusDetails();
	protected abstract GameEvent processSpecificMessage(GameRequest message);
	protected abstract void onStart();
	protected abstract void initialize();
	protected abstract void cleanAfterPlayer(int userId);
	protected abstract GameSettingsChangeResponseDto changeGameSettings(GameSettingsChangeRequest message);
}
