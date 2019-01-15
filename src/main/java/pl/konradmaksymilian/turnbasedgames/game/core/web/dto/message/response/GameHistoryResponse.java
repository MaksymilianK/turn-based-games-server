package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response;

import java.util.List;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.SharedGameEventName;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomResponse;

public class GameHistoryResponse extends RoomResponse {

	private final List<RoomResponse> responses;

	public GameHistoryResponse(List<RoomResponse> responses, long time) {
		super(time);
		this.responses = responses;
	}

	public List<RoomResponse> getResponses() {
		return responses;
	}

	@Override
	public int getCode() {
		return SharedGameEventName.GAME_HISTORY.code();
	}
}
