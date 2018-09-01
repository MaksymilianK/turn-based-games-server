package pl.konradmaksymilian.turnbasedgames.game.room.dto.message;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class UserKickMessage implements RoomMessage {
	
	@NotNull
	private final Integer kickedUserId;
	
	public UserKickMessage(@JsonProperty("kickedUserId") Integer kickedUserId) {
		this.kickedUserId = kickedUserId;
	}

	public int getKickedUserId() {
		return kickedUserId;
	}

	@Override
	public RoomMessageType getType() {
		return RoomMessageType.USER_KICK;
	}
}
