package pl.konradmaksymilian.turnbasedgames.game.core.action.command;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.SharedGameAction;

public final class GameCountdownStartCommand extends HostGameCommand {
	
	@Override
	public int getCode() {
		return SharedGameAction.GAME_COUNTDOWN_START.code();
	}
}
