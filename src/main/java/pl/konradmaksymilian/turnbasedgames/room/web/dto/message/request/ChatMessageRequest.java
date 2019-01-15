package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;

public final class ChatMessageRequest extends NonHostOnlyRoomRequest {

	@NotBlank
	@Size(max = 150)
	private final String text;
	
	public ChatMessageRequest(@JsonProperty("text") String text) {
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
