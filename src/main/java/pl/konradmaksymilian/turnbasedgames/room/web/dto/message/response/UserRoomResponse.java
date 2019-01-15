package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response;

public abstract class UserRoomResponse extends RoomResponse {

	private final int userId;

	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public UserRoomResponse(long time, int userId) {
		super(time);
		this.userId = userId;
	}
	
	public UserRoomResponse(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}
}
