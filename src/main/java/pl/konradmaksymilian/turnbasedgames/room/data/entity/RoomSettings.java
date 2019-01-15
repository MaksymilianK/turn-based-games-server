package pl.konradmaksymilian.turnbasedgames.room.data.entity;

import pl.konradmaksymilian.turnbasedgames.room.data.ChatPolicy;
import pl.konradmaksymilian.turnbasedgames.room.data.RoomPrivacy;

public class RoomSettings {

	private boolean areObserversAllowed;
	private RoomPrivacy privacy;
	private ChatPolicy chatPolicy;
	
	public RoomSettings(boolean areObserversAllowed, RoomPrivacy privacy, ChatPolicy chatPolicy) {
		this.areObserversAllowed = areObserversAllowed;
		this.privacy = privacy;
		this.chatPolicy = chatPolicy;
	}

	public boolean areObserversAllowed() {
		return areObserversAllowed;
	}

	public void setObserversAllowed(boolean areObserversAllowed) {
		this.areObserversAllowed = areObserversAllowed;
	}

	public RoomPrivacy getPrivacy() {
		return privacy;
	}

	public void setPrivacy(RoomPrivacy privacy) {
		this.privacy = privacy;
	}

	public ChatPolicy getChatPolicy() {
		return chatPolicy;
	}

	public void setChatPolicy(ChatPolicy chatPolicy) {
		this.chatPolicy = chatPolicy;
	}
}
