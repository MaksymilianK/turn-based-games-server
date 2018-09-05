package pl.konradmaksymilian.turnbasedgames.game.core.action.event;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.SharedGameAction;

public final class GameStartEvent extends GameEvent {
	
	public GameStartEvent() {}
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public GameStartEvent(long time) {
		super(time);
	}
	
	@Override
	public int getCode() {
		return SharedGameAction.GAME_START.code();
	}
}
