package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request;

public abstract class NonHostOnlyRoomRequest implements RoomRequest {

	@Override
	public final boolean isHostOnly() {
		return false;
	}
}
