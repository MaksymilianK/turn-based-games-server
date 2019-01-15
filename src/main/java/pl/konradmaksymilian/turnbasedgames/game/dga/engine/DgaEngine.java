package pl.konradmaksymilian.turnbasedgames.game.dga.engine;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Set;

import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.GameEngineException;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.PlayerTimeLimitedGameEngine;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.StandardDice;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.event.GameEventManager;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.timer.PlayerTimeLimitedGameTimer;
import pl.konradmaksymilian.turnbasedgames.game.core.data.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.CommonGameEventName;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.DiceRollEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.GameEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.PlayerGameEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameDetailsDto;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsChangeResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameStatusDetailsDto;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request.GameRequest;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request.GameSettingsChangeRequest;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request.SimpleDiceRollRequest;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response.GameSettingsChangeResponse;
import pl.konradmaksymilian.turnbasedgames.game.dga.action.event.DgaMoveEvent;
import pl.konradmaksymilian.turnbasedgames.game.dga.data.BoardSize;
import pl.konradmaksymilian.turnbasedgames.game.dga.engine.board.DgaBoard;
import pl.konradmaksymilian.turnbasedgames.game.dga.engine.player.DgaPlayerManager;
import pl.konradmaksymilian.turnbasedgames.game.dga.web.dto.DgaSettingsChangeResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.dga.web.dto.DgaSettingsRequestDto;
import pl.konradmaksymilian.turnbasedgames.game.dga.web.dto.DgaSettingsResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.dga.web.dto.DgaStatusDetailsDto;
import pl.konradmaksymilian.turnbasedgames.game.dga.web.dto.message.request.DgaMoveRequest;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomResponse;

public final class DgaEngine extends PlayerTimeLimitedGameEngine<DgaPlayerManager> {

	private final DgaBoard board;
	
	private final StandardDice dice;
	
	public DgaEngine(GameEventManager eventManager, PlayerTimeLimitedGameTimer timer,
			DgaPlayerManager playerManager, DgaBoard board, StandardDice dice) {
		super(eventManager, timer, playerManager);
		this.board = board;
		this.dice = dice;
	}

	@Override
	public Game getGame() {
		return Game.DONT_GET_ANGRY;
	}

	@Override
	protected GameEvent processSpecificMessage(GameRequest message) {
		GameEvent eventToPublish = null;
		
		if (message.getCode() == CommonGameEventName.DICE_ROLL.code()) {
			eventToPublish = rollDice((SimpleDiceRollRequest) message);
		} else if (message.getCode() == CommonGameEventName.TOKEN_MOVE.code()) {
			eventToPublish = move((DgaMoveRequest) message);
		} else {
			throw new GameEngineException("Unrecognised request code: " + message.getCode());
		}
		
		return eventToPublish;
	}

	@Override
	protected void initialize() {
		board.initialize(playerManager.getTeams());
	}

	@Override
	protected void cleanAfterPlayer(int team) {
		board.removeTeam(team);
	}

	@Override
	protected GameSettingsChangeResponseDto changeGameSettings(GameSettingsChangeRequest command) {
		var settings = command.getNewSettings();
		if (!settings.getGame().equals(Game.DONT_GET_ANGRY)) {
			throw new BadOperationException("Invalid game settings");
		}
		
		var newSettings = (DgaSettingsRequestDto) settings;
		var settingsChange = new DgaSettingsChangeResponseDto();

		if (newSettings.getBoardSize() != null) {
			board.setSize(newSettings.getBoardSize().fields());
			settingsChange.setBoardSize(newSettings.getBoardSize());
		}
		
		if (newSettings.getMaxPlayers() != null) {
			playerManager.setMaxPlayers(newSettings.getMaxPlayers());
			settingsChange.setMaxPlayers(newSettings.getMaxPlayers());
		}
		
		if (newSettings.getPlayerTime() != null) {
			timer.setPlayerTime(Duration.ofSeconds(newSettings.getPlayerTime()));
			settingsChange.setPlayerTime(newSettings.getPlayerTime());
		}
		
		return settingsChange;
	}
	
	private int countLastDiceRolls() {
		int team = playerManager.getCurrentlyMovingTeam();
		int counter = 0;
		int i = 0;
		while (true) {
			var event = eventManager.findPlayerEventFromTail(i);
			if (event.isPresent() && counter < 2) {
				if (event.get().getTeam() == team) {
					if (event.get().getCode() == CommonGameEventName.DICE_ROLL.code()) {
						counter++;
					}
				} else {
					break;
				}
			} else {
				break;
			}
			
			i++;
		}
		return counter;
	}
	
	private GameEvent rollDice(SimpleDiceRollRequest command) {
		var team = playerManager.getCurrentlyMovingTeam();
		var lastEvent = eventManager.findLastPlayerEvent();
		lastEvent.ifPresent(event -> {
			if (event.getTeam() == team && event.getCode() == CommonGameEventName.DICE_ROLL.code()) {
				throw new BadOperationException("Cannot roll a dice; it is not a valid command at the moment");
			}
		});
		
		var rolledValue = dice.roll();
		
		if (rolledValue == 6) {
			if (countLastDiceRolls() == 2) {
				playerManager.nextTeam();
			}	
		}
		
		return new DiceRollEvent(team, rolledValue);
	}
	
	private GameEvent move(DgaMoveRequest command) {
		var team = playerManager.getCurrentlyMovingTeam();
		var lastEvent = eventManager.findLastPlayerEvent();
		lastEvent.ifPresent(event -> {
			if (event.getTeam() != team || event.getCode() != CommonGameEventName.DICE_ROLL.code()) {
				throw new BadOperationException("Cannot roll a dice; it is not a valid command at the moment");
			}
		});
		
		var lastRoll = (DiceRollEvent) lastEvent.get();
		
		board.enterToken(team, lastRoll.getRolledValue());
		
		if (board.areAllTokensOnFinishFields(team)) {
			finish();
		} else if (lastRoll.getRolledValue() != 6) {
			playerManager.nextTeam();
		}
		
		return new DgaMoveEvent(team, command.getToken());
	}

	@Override
	public GameSettingsResponseDto getSettings() {
		return new DgaSettingsResponseDto(BoardSize.ofFields(board.countFields()), playerManager.getMaxPlayers(), 
				(int) timer.getPlayerTime().toSeconds());
	}

	@Override
	protected GameStatusDetailsDto getStatusDetails() {
		var currentTeamEvents = new ArrayDeque<RoomResponse>();
		var last6Events = eventManager.getLastEvents(6);
		while (last6Events.peek() != null) {
			if (((PlayerGameEvent) last6Events.peek()).getTeam() == playerManager.getCurrentlyMovingTeam()) {
				currentTeamEvents.push(eventManager.convert(last6Events.poll()));
			} else {
				break;
			}
		}
		return new DgaStatusDetailsDto(board.getTokensPositions(), currentTeamEvents);
	}
}
