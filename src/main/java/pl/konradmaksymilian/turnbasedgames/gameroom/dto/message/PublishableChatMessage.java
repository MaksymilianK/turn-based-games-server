package pl.konradmaksymilian.turnbasedgames.gameroom.dto.message;

public final class PublishableChatMessage extends PublishableUserRoomMessage {

	private final String text;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PublishableChatMessage(long time, int senderId, String text) {
		super(time, senderId);
		this.text = text;
	}
	
	public PublishableChatMessage(int senderId, String text) {
		super(senderId);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public RoomMessageType getType() {
		return RoomMessageType.CHAT_MESSAGE;
	}
}
