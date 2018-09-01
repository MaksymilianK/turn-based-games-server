package pl.konradmaksymilian.turnbasedgames.game.core.dto.command;

import java.time.Instant;

public abstract class PublishableGameCommand implements GameCommand {
	
	private final long time;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PublishableGameCommand(long time) {
		this.time = time;
	}
	
	public PublishableGameCommand() {
		this(Instant.now().toEpochMilli());
	}
	
	public long getTime() {
		return time;
	}
}
