package pl.konradmaksymilian.turnbasedgames.game.room.dto.message;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class InvitationMessage implements RoomMessage {

	@NotNull
	@PositiveOrZero
	private final Integer inviteeId;
	
	public InvitationMessage(@JsonProperty("inviteeId") Integer inviteeId) {
		this.inviteeId = inviteeId;
	}

	public int getInviteeId() {
		return inviteeId;
	}

	@Override
	public RoomMessageType getType() {
		return RoomMessageType.INVITATION;
	}
}
