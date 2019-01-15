package pl.konradmaksymilian.turnbasedgames.room.web.dto;

import pl.konradmaksymilian.turnbasedgames.room.data.ChatPolicy;
import pl.konradmaksymilian.turnbasedgames.room.data.RoomPrivacy;

public final class RoomSettingsResponseDto {

	private final boolean areObserversAllowed;
	private final RoomPrivacy privacy;
	private final ChatPolicy chatPolicy;
	
	public RoomSettingsResponseDto(boolean areObserversAllowed, RoomPrivacy privacy, ChatPolicy chatPolicy) {
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
