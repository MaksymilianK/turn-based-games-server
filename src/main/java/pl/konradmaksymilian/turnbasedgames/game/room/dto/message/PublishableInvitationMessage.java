package pl.konradmaksymilian.turnbasedgames.game.room.dto.message;

public final class PublishableInvitationMessage extends PublishableUserRoomMessage {
	
	private final int inviteeId;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PublishableInvitationMessage(long time, int userId, int inviteeId) {
		super(time, userId);
		this.inviteeId = inviteeId;
	}
	
	public PublishableInvitationMessage(int userId, int inviteeId) {
		super(userId);
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
