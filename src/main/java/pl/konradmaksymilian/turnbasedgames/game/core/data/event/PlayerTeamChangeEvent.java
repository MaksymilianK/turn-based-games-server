package pl.konradmaksymilian.turnbasedgames.game.core.data.event;

import java.time.Instant;

public class PlayerTeamChangeEvent extends PlayerGameEvent {
	
	private final int previousTeam;
	private final int newTeam;
	
	public PlayerTeamChangeEvent(int previousTeam, int newTeam, int senderTeam) {
		super(senderTeam);
		this.previousTeam = previousTeam;
		this.newTeam = newTeam;
	}
	
	public PlayerTeamChangeEvent(Instant time, int previousTeam, int newTeam, int senderTeam) {
		super(time, senderTeam);
		this.previousTeam = previousTeam;
		this.newTeam = newTeam;
	}
		
	public int getPreviousTeam() {
		return previousTeam;
	}

	public int getNewTeam() {
		return newTeam;
	}

	@Override
	public int getCode() {
		return SharedGameEventName.GAME_START.code();
	}
}
