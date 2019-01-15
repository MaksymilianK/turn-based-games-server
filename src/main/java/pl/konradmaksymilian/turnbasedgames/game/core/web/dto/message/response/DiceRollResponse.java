package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.CommonGameEventName;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomResponse;

public final class DiceRollResponse extends RoomResponse {

	private final int rolledValue;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public DiceRollResponse(int rolledValue, long time) {
		super(time);
		this.rolledValue = rolledValue;
		
	}
	
	public int getRolledValue() {
		return rolledValue;
	}

	@Override
	public int getCode() {
		return CommonGameEventName.DICE_ROLL.code();
	}
}
