package pl.konradmaksymilian.turnbasedgames.game.room.dto.message;

public final class PublishableUserEscapeMessage extends PublishableUserRoomMessage {
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PublishableUserEscapeMessage(long time, int userId) {
		super(time, userId);
	}
	
	public PublishableUserEscapeMessage(int userId) {
		super(userId);
	}

	@Override
	public RoomMessageType getType() {
		return RoomMessageType.USER_ESCAPE;
	}
}
