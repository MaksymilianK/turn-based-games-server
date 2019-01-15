package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response;

import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;

public final class ChatMessageResponse extends UserRoomResponse {

	private final String text;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public ChatMessageResponse(long time, int senderId, String text) {
		super(time, senderId);
		this.text = text;
	}
	
	public ChatMessageResponse(int senderId, String text) {
		super(senderId);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public int getCode() {
		return SharedRoomMessageName.CHAT_MESSAGE.code();
	}
}
