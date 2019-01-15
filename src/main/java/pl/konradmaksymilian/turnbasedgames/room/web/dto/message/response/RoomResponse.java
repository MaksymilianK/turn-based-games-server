package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response;

import java.time.Instant;

import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomMessage;

public abstract class RoomResponse implements RoomMessage {

	private final long time;

	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public RoomResponse(long time) {
		this.time = time;
	}
	
	public RoomResponse() {
		this(Instant.now().toEpochMilli());
	}

	public long getTime() {
		return time;
	}
}
