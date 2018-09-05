package pl.konradmaksymilian.turnbasedgames.gameroom.dto.message;

public final class PublishableHostChangeMessage extends PublishableRoomMessage {

	private int newHostId;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PublishableHostChangeMessage(long time, int newHostId) {
		super(time);
		this.newHostId = newHostId;
	}

	public PublishableHostChangeMessage(int newHostId) {
		this.newHostId = newHostId;
	}
	
	public int getNewHostId() {
		return newHostId;
	}

	@Override
	public RoomMessageType getType() {
		return RoomMessageType.HOST_CHANGE;
	}
}
