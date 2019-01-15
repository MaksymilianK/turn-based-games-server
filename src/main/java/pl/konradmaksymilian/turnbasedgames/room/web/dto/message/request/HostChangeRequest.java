package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;

public final class HostChangeRequest extends HostOnlyRoomRequest {

	@NotNull
	private Integer newHostId;
	
	public HostChangeRequest(@JsonProperty("newHostId") Integer newHostId) {
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
