package pl.konradmaksymilian.turnbasedgames.gameroom.dto.message;

public final class PublishableUserJoinMessage extends PublishableUserRoomMessage {
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PublishableUserJoinMessage(long time, int userId) {
		super(time, userId);
	}
	
	public PublishableUserJoinMessage(int userId) {
		super(userId);
	}

	@Override
	public RoomMessageType getType() {
		return RoomMessageType.USER_JOIN;
	}
}
