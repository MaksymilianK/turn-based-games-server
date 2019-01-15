package pl.konradmaksymilian.turnbasedgames.game.core.data.event;

import java.time.Instant;

public abstract class PlayerGameEvent extends GameEvent {

	private final int team;
	
	public PlayerGameEvent(int team) {
		this.team = team;
	}
	
	public PlayerGameEvent(Instant time, int team) {
		super(time);
		this.team = team;
	}

	public int getTeam() {
		return team;
	}
}
