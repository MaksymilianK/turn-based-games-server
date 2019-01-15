package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response;

import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;

public final class HostChangeResponse extends RoomResponse {

	private int newHostId;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public HostChangeResponse(long time, int newHostId) {
		super(time);
		this.newHostId = newHostId;
	}

	public HostChangeResponse(int newHostId) {
		this.newHostId = newHostId;
	}
	
	public int getNewHostId() {
		return newHostId;
	}

	@Override
	public int getCode() {
		return SharedRoomMessageName.HOST_CHANGE.code();
	}
}
