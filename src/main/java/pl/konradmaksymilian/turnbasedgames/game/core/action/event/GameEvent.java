package pl.konradmaksymilian.turnbasedgames.game.core.action.event;

import java.time.Instant;

import pl.konradmaksymilian.turnbasedgames.game.core.action.GameAction;

public abstract class GameEvent implements GameAction {

	private final long time;
	
	public GameEvent() {
		this(Instant.now().toEpochMilli());
	}
	
	/**
 	* @param time the message sending time (millis from epoch)
 	*/
	public GameEvent(long time) {
		this.time = time;
	}
}
