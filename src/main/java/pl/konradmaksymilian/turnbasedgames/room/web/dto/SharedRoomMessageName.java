package pl.konradmaksymilian.turnbasedgames.room.web.dto;

public enum SharedRoomMessageName {

	CHAT_MESSAGE,
	INVITATION,
	GAME_CHANGE,
	ROOM_SETTINGS_CHANGE,
	USER_KICK,
	USER_JOIN,
	USER_ESCAPE,
	HOST_CHANGE,
	ROOM_STATUS;
	
	public int code() {
		return this.ordinal();
	}
}
