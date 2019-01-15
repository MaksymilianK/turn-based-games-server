package pl.konradmaksymilian.turnbasedgames.game.dga.web.dto;

import java.util.Map;
import java.util.Queue;

import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameStatusDetailsDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomResponse;

public class DgaStatusDetailsDto implements GameStatusDetailsDto {

	private final Map<Integer, Map<Integer, Integer>> tokensPositions;
	private final Queue<RoomResponse> lastEvents;
	
	public DgaStatusDetailsDto(Map<Integer, Map<Integer, Integer>> tokensPositions, Queue<RoomResponse> lastEvents) {
		this.tokensPositions = tokensPositions;
		this.lastEvents = lastEvents;
	}

	public Map<Integer, Map<Integer, Integer>> getTokensPositions() {
		return tokensPositions;
	}

	public Queue<RoomResponse> getLastEvents() {
		return lastEvents;
	}
}