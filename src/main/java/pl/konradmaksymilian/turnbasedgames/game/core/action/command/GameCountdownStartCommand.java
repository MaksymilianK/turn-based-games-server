package pl.konradmaksymilian.turnbasedgames.game.core.action.command;

import pl.konradmaksymilian.turnbasedgames.game.core.action.SharedGameActionName;

public final class GameCountdownStartCommand extends HostGameCommand {
	
	@Override
	public int getCode() {
		return SharedGameActionName.GAME_COUNTDOWN_START.code();
	}
}
