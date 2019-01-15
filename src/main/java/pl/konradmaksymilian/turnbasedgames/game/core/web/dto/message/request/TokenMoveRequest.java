package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.CommonGameEventName;

public final class TokenMoveRequest extends NonHostOnlyGameRequest {

	@Override
	public int getCode() {
		return CommonGameEventName.TOKEN_MOVE.code();
	}
}
