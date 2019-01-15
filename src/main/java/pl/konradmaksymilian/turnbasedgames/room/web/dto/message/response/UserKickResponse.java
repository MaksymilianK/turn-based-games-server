package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response;

import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;

public final class UserKickResponse extends UserRoomResponse {
	
	private final int kickedUserId;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public UserKickResponse(long time, int userId, int kickedUserId) {
		super(time, userId);
		this.kickedUserId = kickedUserId;
	}
	
	public UserKickResponse(int userId, int kickedUserId) {
		super(userId);
		this.kickedUserId = kickedUserId;
	}

	public int getKickedUserId() {
		return kickedUserId;
	}

	@Override
	public int getCode() {
		return SharedRoomMessageName.USER_KICK.code();
	}
}
