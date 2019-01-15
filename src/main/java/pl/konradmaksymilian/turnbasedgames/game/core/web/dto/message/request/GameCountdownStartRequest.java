package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.SharedGameEventName;

public final class GameCountdownStartRequest extends HostOnlyGameRequest {
	
	@Override
	public int getCode() {
		return SharedGameEventName.GAME_COUNTDOWN_START.code();
	}
}
