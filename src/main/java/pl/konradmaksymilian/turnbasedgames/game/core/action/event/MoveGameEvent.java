package pl.konradmaksymilian.turnbasedgames.game.core.action.event;

import java.time.Instant;

public abstract class MoveGameEvent extends GameEvent {
	
	private final int team;
	
	public MoveGameEvent(int team) {
		this.team = team;
	}
	
	public MoveGameEvent(Instant time, int team) {
		super(time);
		this.team = team;
	}
	
	public int getTeam() {
		return team;
	}
}
