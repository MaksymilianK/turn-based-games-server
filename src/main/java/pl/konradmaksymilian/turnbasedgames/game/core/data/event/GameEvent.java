package pl.konradmaksymilian.turnbasedgames.game.core.data.event;

import java.time.Instant;

public abstract class GameEvent {

	private final Instant time;
	
	public GameEvent() {
		this(Instant.now());
	}
	
	public GameEvent(Instant time) {
		this.time = time;
	}

	public Instant getTime() {
		return time;
	}
	
	public abstract int getCode();
}
