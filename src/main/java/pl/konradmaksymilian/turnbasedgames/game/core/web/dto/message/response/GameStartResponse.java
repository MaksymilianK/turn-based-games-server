package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.SharedGameEventName;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomResponse;

public final class GameStartResponse extends RoomResponse {
		
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public GameStartResponse(long time) {
		super(time);
	}
	
	@Override
	public int getCode() {
		return SharedGameEventName.GAME_START.code();
	}
}
