package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request;

import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomMessage;

public interface RoomRequest extends RoomMessage {

	public abstract boolean isHostOnly();
}
