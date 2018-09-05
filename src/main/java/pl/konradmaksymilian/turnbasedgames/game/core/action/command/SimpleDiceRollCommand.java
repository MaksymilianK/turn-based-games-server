package pl.konradmaksymilian.turnbasedgames.game.core.action.command;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.CommonGameActionName;

public final class SimpleDiceRollCommand extends NotHostGameCommand {

	@Override
	public int getCode() {
		return CommonGameActionName.DICE_ROLL.code();
	}
}
