package pl.konradmaksymilian.turnbasedgames.game.core.action.event;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.CommonGameActionName;

public final class SimpleMoveEvent extends GameEvent {

	public SimpleMoveEvent() {}
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public SimpleMoveEvent(long time) {
		super(time);
	}
	
	@Override
	public int getCode() {
		return CommonGameActionName.MOVE.code();
	}
}
