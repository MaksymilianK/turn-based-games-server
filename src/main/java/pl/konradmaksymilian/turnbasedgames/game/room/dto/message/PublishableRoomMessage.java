package pl.konradmaksymilian.turnbasedgames.game.room.dto.message;

import java.time.Instant;

public abstract class PublishableRoomMessage implements RoomMessage {

	private final long time;

	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PublishableRoomMessage(long time) {
		this.time = time;
	}
	
	public PublishableRoomMessage() {
		this(Instant.now().toEpochMilli());
	}

	public long getTime() {
		return time;
	}
}
