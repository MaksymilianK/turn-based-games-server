package pl.konradmaksymilian.turnbasedgames.game.room;

import java.time.Instant;

public final class Invitation {

	private final int senderId;
	
	private final int inviteeId;
	
	private final Instant time;

	public Invitation(int invitorId, int invitedId) {
		this(invitorId, invitedId, Instant.now());
	}
	
	public Invitation(int senderId, int inviteeId, Instant time) {
		this.senderId = senderId;
		this.inviteeId = inviteeId;
		this.time = time;
	}

	public int getSenderId() {
		return senderId;
	}

	public int getInviteeId() {
		return inviteeId;
	}

	public Instant getTime() {
		return time;
	}
}
