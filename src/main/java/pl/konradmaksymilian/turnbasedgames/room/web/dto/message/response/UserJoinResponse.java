package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response;

import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;

public final class UserJoinResponse extends UserRoomResponse {
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public UserJoinResponse(long time, int userId) {
		super(time, userId);
	}
	
	public UserJoinResponse(int userId) {
		super(userId);
	}

	@Override
	public int getCode() {
		return SharedRoomMessageName.USER_JOIN.code();
	}
}
