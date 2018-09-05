package pl.konradmaksymilian.turnbasedgames.game.core.action.command;

public abstract class NotHostGameCommand extends GameCommand {

	@Override
	public boolean isHostCommand() {
		return false;
	}
}
