package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;

public final class UserKickRequest extends HostOnlyRoomRequest {
	
	@NotNull
	private final Integer kickedUserId;
	
	public UserKickRequest(@JsonProperty("kickedUserId") Integer kickedUserId) {
		this.kickedUserId = kickedUserId;
	}

	public int getKickedUserId() {
		return kickedUserId;
	}

	@Override
	public int getCode() {
		return SharedRoomMessageName.USER_KICK.code();
	}
}
