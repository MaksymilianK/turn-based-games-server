package pl.konradmaksymilian.turnbasedgames.game.core.dto.event;

import pl.konradmaksymilian.turnbasedgames.game.core.action.GameAction;

public abstract class GameEventDto implements GameAction {
	
	private final long time;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public GameEventDto(long time) {
		this.time = time;
	}
	
	/**
	 * @return the message sending time (millis from epoch)
	 */
	public long getTime() {
		return time;
	}
}
