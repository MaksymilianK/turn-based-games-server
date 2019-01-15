package pl.konradmaksymilian.turnbasedgames.room.web.dto;

public class InvitationResponseDto {
	
	private final int senderId;
	private final int inviteeId;
	
	public InvitationResponseDto(int senderId, int inviteeId) {
		this.senderId = senderId;
		this.inviteeId = inviteeId;
	}

	public int getSenderId() {
		return senderId;
	}
	
	public int getInviteeId() {
		return inviteeId;
	}
}
