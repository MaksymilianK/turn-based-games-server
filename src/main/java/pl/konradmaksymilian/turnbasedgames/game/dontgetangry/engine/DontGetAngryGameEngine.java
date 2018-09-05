package pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine;

import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.GameCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.GameSettingsChangeCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.SimpleDiceRollCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.action.event.GameEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.CommonGameActionName;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.EventManager;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.PlayerTimeLimitedGameEngine;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.timer.PlayerTimeLimitedGameTimer;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.action.command.DontGetAngryMoveCommand;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine.board.DontGetAngryBoard;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine.player.DontGetAngryPlayerManager;

public final class DontGetAngryGameEngine extends PlayerTimeLimitedGameEngine<DontGetAngryPlayerManager> {

	private final DontGetAngryBoard board;
	
	public DontGetAngryGameEngine(EventManager eventManager, PlayerTimeLimitedGameTimer timer,
			DontGetAngryPlayerManager playerManager, DontGetAngryBoard board) {
		super(eventManager, timer, playerManager);
		this.board = board;
	}

	@Override
	public Game getGame() {
		return Game.DONT_GET_ANGRY;
	}

	@Override
	protected GameEvent processSpecificCommand(GameCommand command) {
		GameEvent eventToPublish = null;
		
		if (command.getCode() == CommonGameActionName.DICE_ROLL.code()) {
			eventToPublish = rollDice((SimpleDiceRollCommand) command);
		} else if (command.getCode() == CommonGameActionName.MOVE.code()) {
			eventToPublish = move((DontGetAngryMoveCommand) command);
		} else {
			cannotRecogniseCommand();
		}
		
		return eventToPublish;
	}

	@Override
	protected void initialize() {
		
	}

	@Override
	protected void cleanAfterPlayer(int userId) {
		
	}

	@Override
	protected void changeGameSettings(GameSettingsChangeCommand command) {
		
	}
	
	private GameEvent rollDice(SimpleDiceRollCommand command) {
		return null;
	}
	
	private GameEvent move(DontGetAngryMoveCommand command) {
		return null;
	}
}
