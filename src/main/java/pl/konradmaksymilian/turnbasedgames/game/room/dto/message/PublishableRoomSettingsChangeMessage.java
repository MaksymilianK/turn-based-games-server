package pl.konradmaksymilian.turnbasedgames.game.room.dto.message;

import pl.konradmaksymilian.turnbasedgames.game.room.ChatPolicy;
import pl.konradmaksymilian.turnbasedgames.game.room.RoomPrivacy;

public final class PublishableRoomSettingsChangeMessage extends PublishableRoomMessage {

	private final Boolean areObserversAllowed;
	
	private final RoomPrivacy privacy;
	
	private final ChatPolicy chatPolicy;

	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PublishableRoomSettingsChangeMessage(long time, Boolean areObserversAllowed, RoomPrivacy privacy,
			ChatPolicy chatPolicy) {
		super(time);
		this.areObserversAllowed = areObserversAllowed;
		this.privacy = privacy;
		this.chatPolicy = chatPolicy;
	}
	
	public PublishableRoomSettingsChangeMessage(Boolean areObserversAllowed, RoomPrivacy privacy, ChatPolicy chatPolicy) {
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
