package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.CommonGameEventName;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomResponse;

public final class IdentifiableTokenMoveResponse extends RoomResponse  {

	private final int token;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public IdentifiableTokenMoveResponse(int token, long time) {
		super(time);
		this.token = token;
	}
	
	public int getToken() {
		return token;
	}

	@Override
	public int getCode() {
		return CommonGameEventName.TOKEN_MOVE.code();
	}
}
