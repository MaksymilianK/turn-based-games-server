package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request;

public abstract class HostOnlyRoomRequest implements RoomRequest {

	@Override
	public final boolean isHostOnly() {
		return true;
	}
}
