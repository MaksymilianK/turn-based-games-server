package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response;

import java.util.List;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.SharedGameEventName;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomResponse;

public final class GameFinishResponse extends RoomResponse {

	private final List<RoomResponse> gameHistory;

	/**
 	* @param time the message sending time (millis from epoch)
 	*/
	public GameFinishResponse(List<RoomResponse> gameHistory, long time) {
		super(time);
		this.gameHistory = gameHistory;
	}
	
	public List<RoomResponse> getGameHistory() {
		return gameHistory;
	}

	@Override
	public int getCode() {
		return SharedGameEventName.GAME_FINISH.code();
	}
}
