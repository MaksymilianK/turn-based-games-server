package pl.konradmaksymilian.turnbasedgames.game.dga.web.dto.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.CommonGameEventName;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request.NonHostOnlyGameRequest;

public final class DgaMoveRequest extends NonHostOnlyGameRequest {

	private final int token;
	
	public DgaMoveRequest(@JsonProperty("token") int token) {
		this.token = token;
	}
	
	public int getToken() {
		return token;
	}
	
	@Override
	public int getCode() {
		return CommonGameEventName.TOKEN_MOVE.code();
	}
}
