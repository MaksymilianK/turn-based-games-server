package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.room.data.ChatPolicy;
import pl.konradmaksymilian.turnbasedgames.room.data.RoomPrivacy;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;

public final class RoomSettingsChangeRequest extends HostOnlyRoomRequest {

	private final Boolean areObserversAllowed;
	
	private final RoomPrivacy privacy;
	
	private final ChatPolicy chatPolicy;

	public RoomSettingsChangeRequest(@JsonProperty("areObserversAllowed") Boolean areObserversAllowed, 
			@JsonProperty("privacy") RoomPrivacy privacy, @JsonProperty("chatPolicy") ChatPolicy chatPolicy) {
		this.areObserversAllowed = areObserversAllowed;
		this.privacy = privacy;
		this.chatPolicy = chatPolicy;
	}

	public Boolean areObserversAllowed() {
		return areObserversAllowed;
	}

	public RoomPrivacy getPrivacy() {
		return privacy;
	}

	public ChatPolicy getChatPolicy() {
		return chatPolicy;
	}

	@Override
	public int getCode() {
		return SharedRoomMessageName.ROOM_SETTINGS_CHANGE.code();
	}
}
