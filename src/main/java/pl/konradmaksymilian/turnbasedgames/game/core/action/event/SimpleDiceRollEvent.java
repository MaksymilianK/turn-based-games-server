package pl.konradmaksymilian.turnbasedgames.game.core.action.event;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.CommonGameActionName;

public final class SimpleDiceRollEvent extends GameEvent {

	public SimpleDiceRollEvent() {}
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public SimpleDiceRollEvent(long time) {
		super(time);
	}
	
	@Override
	public int getCode() {
		return CommonGameActionName.DICE_ROLL.code();
	}
}
