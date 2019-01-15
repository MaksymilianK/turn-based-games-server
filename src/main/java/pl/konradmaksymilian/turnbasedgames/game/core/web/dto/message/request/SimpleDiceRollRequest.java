package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.CommonGameEventName;

public final class SimpleDiceRollRequest extends NonHostOnlyGameRequest {

	@Override
	public int getCode() {
		return CommonGameEventName.DICE_ROLL.code();
	}
}
