package pl.konradmaksymilian.turnbasedgames.room.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.room.data.ChatPolicy;
import pl.konradmaksymilian.turnbasedgames.room.data.RoomPrivacy;

public final class RoomSettingsRequestDto {

	private final Boolean areObserversAllowed;
	private final RoomPrivacy privacy;
	private final ChatPolicy chatPolicy;
	
	public RoomSettingsRequestDto(@JsonProperty("areObserversAllowed") Boolean areObserversAllowed, 
			@JsonProperty("private") RoomPrivacy privacy, @JsonProperty("chatPolicy") ChatPolicy chatPolicy) {
		this.areObserversAllowed = areObserversAllowed;
		this.privacy = privacy;
		this.chatPolicy = chatPolicy;
	}

	public boolean isAreObserversAllowed() {
		return areObserversAllowed;
	}

	public RoomPrivacy getPrivacy() {
		return privacy;
	}

	public ChatPolicy getChatPolicy() {
		return chatPolicy;
	}
}
