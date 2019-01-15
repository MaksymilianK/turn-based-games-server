package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;

public final class InvitationRequest extends NonHostOnlyRoomRequest {

	@NotNull
	@PositiveOrZero
	private final Integer inviteeId;
	
	public InvitationRequest(@JsonProperty("inviteeId") Integer inviteeId) {
		this.inviteeId = inviteeId;
	}

	public int getInviteeId() {
		return inviteeId;
	}

	@Override
	public int getCode() {
		return SharedRoomMessageName.INVITATION.code();
	}
}
