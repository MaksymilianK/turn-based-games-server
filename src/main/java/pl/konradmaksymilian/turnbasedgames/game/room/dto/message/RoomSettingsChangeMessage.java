package pl.konradmaksymilian.turnbasedgames.game.room.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.room.ChatPolicy;
import pl.konradmaksymilian.turnbasedgames.game.room.RoomPrivacy;
import pl.konradmaksymilian.turnbasedgames.game.validator.NotEmptyRoomSettingsChangeMessage;

@NotEmptyRoomSettingsChangeMessage
public final class RoomSettingsChangeMessage implements RoomMessage {

	private final Boolean areObserversAllowed;
	
	private final RoomPrivacy privacy;
	
	private final ChatPolicy chatPolicy;

	public RoomSettingsChangeMessage(@JsonProperty("areObserversAllowed") Boolean areObserversAllowed, 
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
	public RoomMessageType getType() {
		return RoomMessageType.ROOM_SETTINGS_CHANGE;
	}
}
