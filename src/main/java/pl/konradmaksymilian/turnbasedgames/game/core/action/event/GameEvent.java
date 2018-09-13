package pl.konradmaksymilian.turnbasedgames.game.core.action.event;

import java.time.Instant;

import pl.konradmaksymilian.turnbasedgames.game.core.action.GameAction;

public abstract class GameEvent implements GameAction {

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
}
