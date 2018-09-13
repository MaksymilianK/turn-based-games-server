package pl.konradmaksymilian.turnbasedgames.game.core.action.event;

import java.time.Instant;

import pl.konradmaksymilian.turnbasedgames.game.core.action.SharedGameActionName;

public class PlayerTeamChangeEvent extends GameEvent {
	
	private final int previousTeam;
	
	private final int newTeam;
	
	public PlayerTeamChangeEvent(int previousTeam, int newTeam) {
		this.previousTeam = previousTeam;
		this.newTeam = newTeam;
	}
	
	public PlayerTeamChangeEvent(Instant time, int previousTeam, int newTeam) {
		super(time);
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
		return SharedGameActionName.GAME_START.code();
	}
}
