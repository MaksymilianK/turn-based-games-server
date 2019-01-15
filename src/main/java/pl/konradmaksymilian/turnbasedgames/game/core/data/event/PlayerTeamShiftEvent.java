package pl.konradmaksymilian.turnbasedgames.game.core.data.event;

import java.time.Instant;

public final class PlayerTeamShiftEvent extends PlayerGameEvent {
	
	private final int previousTeam;
	
	private final int newTeam;
	
	public PlayerTeamShiftEvent(int previousTeam, int newTeam, int senderTeam) {
		super(senderTeam);
		this.previousTeam = previousTeam;
		this.newTeam = newTeam;
	}
	
	public PlayerTeamShiftEvent(Instant time, int previousTeam, int newTeam, int senderTeam) {
		super(time, senderTeam);
		this.previousTeam = previousTeam;
		this.newTeam = newTeam;
	}

	public Integer getPreviousTeam() {
		return previousTeam;
	}

	public Integer getNewTeam() {
		return newTeam;
	}

	@Override
	public int getCode() {
		return SharedGameEventName.PLAYER_TEAM_SHIFT.code();
	}
}
