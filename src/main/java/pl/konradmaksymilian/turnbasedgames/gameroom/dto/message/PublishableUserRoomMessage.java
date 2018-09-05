package pl.konradmaksymilian.turnbasedgames.gameroom.dto.message;

public abstract class PublishableUserRoomMessage extends PublishableRoomMessage {

	private final int userId;

	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PublishableUserRoomMessage(long time, int userId) {
		super(time);
		this.userId = userId;
	}
	
	public PublishableUserRoomMessage(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}
}
