package pl.konradmaksymilian.turnbasedgames.room.web.dto;

import java.util.Map;

import pl.konradmaksymilian.turnbasedgames.game.core.data.Game;

public class RoomsStatsResponseDto {

	private final Map<Game, Integer> rooms;
	private final Map<Game, Integer> players;
	
	public RoomsStatsResponseDto(Map<Game, Integer> rooms, Map<Game, Integer> players) {
		this.rooms = rooms;
		this.players = players;
	}

	public Map<Game, Integer> getRooms() {
		return rooms;
	}

	public Map<Game, Integer> getPlayers() {
		return players;
	}
}
