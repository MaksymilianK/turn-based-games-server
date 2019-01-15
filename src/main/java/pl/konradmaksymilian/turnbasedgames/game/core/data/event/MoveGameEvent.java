package pl.konradmaksymilian.turnbasedgames.game.core.data.event;

import java.time.Instant;

public abstract class MoveGameEvent extends PlayerGameEvent {
		
	public MoveGameEvent(int team) {
		super(team);
	}
	
	public MoveGameEvent(Instant time, int team) {
		super(time, team);
	}
	
}
