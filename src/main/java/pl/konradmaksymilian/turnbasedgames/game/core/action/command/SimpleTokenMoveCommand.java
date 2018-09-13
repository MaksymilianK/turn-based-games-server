package pl.konradmaksymilian.turnbasedgames.game.core.action.command;

import pl.konradmaksymilian.turnbasedgames.game.core.action.CommonGameActionName;

public final class SimpleTokenMoveCommand extends NotHostGameCommand {

	@Override
	public int getCode() {
		return CommonGameActionName.TOKEN_MOVE.code();
	}
}
