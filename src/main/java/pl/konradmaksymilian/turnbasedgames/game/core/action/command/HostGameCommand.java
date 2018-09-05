package pl.konradmaksymilian.turnbasedgames.game.core.action.command;

public abstract class HostGameCommand extends GameCommand {

	@Override
	public boolean isHostCommand() {
		return true;
	}

}
