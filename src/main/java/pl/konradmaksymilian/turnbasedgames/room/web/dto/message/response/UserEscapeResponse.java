package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response;

import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;

public final class UserEscapeResponse extends UserRoomResponse {
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public UserEscapeResponse(long time, int userId) {
		super(time, userId);
	}
	
	public UserEscapeResponse(int userId) {
		super(userId);
	}

	@Override
	public int getCode() {
		return SharedRoomMessageName.USER_ESCAPE.code();
	}
}
