package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.CommonGameEventName;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomResponse;

public final class PlayerEscapeResponse extends RoomResponse {

	private final int team;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PlayerEscapeResponse(int team, long time) {
		super(time);
		this.team = team;
		
	}
	
	public int getTeam() {
		return team;
	}

	@Override
	public int getCode() {
		return CommonGameEventName.DICE_ROLL.code();
	}
}
