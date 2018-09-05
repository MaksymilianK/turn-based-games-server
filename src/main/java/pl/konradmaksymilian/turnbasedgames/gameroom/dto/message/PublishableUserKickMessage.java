package pl.konradmaksymilian.turnbasedgames.gameroom.dto.message;

public final class PublishableUserKickMessage extends PublishableUserRoomMessage {
	
	private final int kickedUserId;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PublishableUserKickMessage(long time, int userId, int kickedUserId) {
		super(time, userId);
		this.kickedUserId = kickedUserId;
	}
	
	public PublishableUserKickMessage(int userId, int kickedUserId) {
		super(userId);
		this.kickedUserId = kickedUserId;
	}

	public int getKickedUserId() {
		return kickedUserId;
	}

	@Override
	public RoomMessageType getType() {
		return RoomMessageType.USER_KICK;
	}
}
