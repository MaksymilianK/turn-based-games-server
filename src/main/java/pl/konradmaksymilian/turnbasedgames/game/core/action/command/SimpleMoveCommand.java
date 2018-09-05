package pl.konradmaksymilian.turnbasedgames.game.core.action.command;

import pl.konradmaksymilian.turnbasedgames.game.core.action.command.NotHostGameCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.CommonGameActionName;

public final class SimpleMoveCommand extends NotHostGameCommand {

	@Override
	public int getCode() {
		return CommonGameActionName.MOVE.code();
	}
}
