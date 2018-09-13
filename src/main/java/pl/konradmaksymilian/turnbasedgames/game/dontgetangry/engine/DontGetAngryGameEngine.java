package pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine;

import java.time.Duration;

import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;
import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.action.CommonGameActionName;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.GameCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.GameSettingsChangeCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.SimpleDiceRollCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.action.event.DiceRollEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.action.event.GameEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.EventManager;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.PlayerTimeLimitedGameEngine;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.timer.PlayerTimeLimitedGameTimer;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.utils.StandardDice;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.action.command.DontGetAngryMoveCommand;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.action.event.DontGetAngryMoveEvent;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.dto.DontGetAngryGameSettingsDto;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine.board.DontGetAngryBoard;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine.player.DontGetAngryPlayerManager;

public final class DontGetAngryGameEngine extends PlayerTimeLimitedGameEngine<DontGetAngryPlayerManager> {

	private final DontGetAngryBoard board;
	
	private final StandardDice dice;
	
	public DontGetAngryGameEngine(EventManager eventManager, PlayerTimeLimitedGameTimer timer,
			DontGetAngryPlayerManager playerManager, DontGetAngryBoard board, StandardDice dice) {
		super(eventManager, timer, playerManager);
		this.board = board;
		this.dice = dice;
	}

	@Override
	public Game getGame() {
		return Game.DONT_GET_ANGRY;
	}
	
	public DontGetAngryBoard getBoard() {
		return board;
	}
	
	public int countLastDiceRolls() {
		int team = playerManager.getCurrentlyMovingTeam();
		int counter = 0;
		int i = 0;
		while (true) {
			var event = eventManager.findMoveEventFromTail(i);
			if (event.isPresent() && counter < 2) {
				if (event.get().getTeam() == team) {
					if (event.get().getCode() == CommonGameActionName.DICE_ROLL.code()) {
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

	@Override
	protected GameEvent processSpecificCommand(GameCommand command) {
		GameEvent eventToPublish = null;
		
		if (command.getCode() == CommonGameActionName.DICE_ROLL.code()) {
			eventToPublish = rollDice((SimpleDiceRollCommand) command);
		} else if (command.getCode() == CommonGameActionName.TOKEN_MOVE.code()) {
			eventToPublish = move((DontGetAngryMoveCommand) command);
		} else {
			cannotRecogniseCommand();
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
	protected void changeGameSettings(GameSettingsChangeCommand command) {
		var settings = command.getNewSettings();
		if (!settings.getGame().equals(Game.DONT_GET_ANGRY)) {
			throw new BadOperationException("Invalid game settings");
		}
		
		var dontGetAngrySettings = (DontGetAngryGameSettingsDto) settings;
		
		if (dontGetAngrySettings.getBoardSize() != null) {
			board.setSize(dontGetAngrySettings.getBoardSize());
		}
		
		if (dontGetAngrySettings.getMaxPlayers() != null) {
			playerManager.setMaxPlayers(dontGetAngrySettings.getMaxPlayers());
		}
		
		if (dontGetAngrySettings.getPlayerTime() != null) {
			timer.setPlayerTime(Duration.ofSeconds(dontGetAngrySettings.getPlayerTime()));
		}
	}
	
	private GameEvent rollDice(SimpleDiceRollCommand command) {
		var team = playerManager.getCurrentlyMovingTeam();
		var lastEvent = eventManager.findLastMoveEvent();
		lastEvent.ifPresent(event -> {
			if (event.getTeam() == team && event.getCode() == CommonGameActionName.DICE_ROLL.code()) {
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
	
	private GameEvent move(DontGetAngryMoveCommand command) {
		var team = playerManager.getCurrentlyMovingTeam();
		var lastEvent = eventManager.findLastMoveEvent();
		lastEvent.ifPresent(event -> {
			if (event.getTeam() != team || event.getCode() != CommonGameActionName.DICE_ROLL.code()) {
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
		
		return new DontGetAngryMoveEvent(team, command.getToken());
	}
}
