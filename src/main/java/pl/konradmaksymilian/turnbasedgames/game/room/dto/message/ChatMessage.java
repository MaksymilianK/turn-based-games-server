package pl.konradmaksymilian.turnbasedgames.game.room.dto.message;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class ChatMessage implements RoomMessage {

	@NotBlank
	@Size(max = 150)
	private final String text;
	
	public ChatMessage(@JsonProperty("text") String text) {
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
