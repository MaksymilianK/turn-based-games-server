package pl.konradmaksymilian.turnbasedgames.gameroom.dto.message;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class HostChangeMessage implements RoomMessage {

	@NotNull
	private Integer newHostId;
	
	public HostChangeMessage(@JsonProperty("newHostId") Integer newHostId) {
		this.newHostId = newHostId;
	}

	public int getNewHostId() {
		return newHostId;
	}

	@Override
	public RoomMessageType getType() {
		return RoomMessageType.HOST_CHANGE;
	}
}
