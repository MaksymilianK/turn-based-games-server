package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response;

import pl.konradmaksymilian.turnbasedgames.room.data.ChatPolicy;
import pl.konradmaksymilian.turnbasedgames.room.data.RoomPrivacy;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;

public final class RoomSettingsChangeResponse extends RoomResponse {

	private final Boolean areObserversAllowed;
	
	private final RoomPrivacy privacy;
	
	private final ChatPolicy chatPolicy;

	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public RoomSettingsChangeResponse(long time, Boolean areObserversAllowed, RoomPrivacy privacy,
			ChatPolicy chatPolicy) {
		super(time);
		this.areObserversAllowed = areObserversAllowed;
		this.privacy = privacy;
		this.chatPolicy = chatPolicy;
	}
	
	public RoomSettingsChangeResponse(Boolean areObserversAllowed, RoomPrivacy privacy, ChatPolicy chatPolicy) {
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
