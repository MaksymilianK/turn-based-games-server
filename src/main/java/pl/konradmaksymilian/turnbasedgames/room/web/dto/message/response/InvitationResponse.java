package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response;

import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;

public final class InvitationResponse extends UserRoomResponse {
	
	private final int inviteeId;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public InvitationResponse(long time, int userId, int inviteeId) {
		super(time, userId);
		this.inviteeId = inviteeId;
	}
	
	public InvitationResponse(int userId, int inviteeId) {
		super(userId);
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
