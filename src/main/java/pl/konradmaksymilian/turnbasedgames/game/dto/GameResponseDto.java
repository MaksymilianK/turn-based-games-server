package pl.konradmaksymilian.turnbasedgames.game.dto;

import java.util.Map;

import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameStatus;

public abstract class GameResponseDto {

	private final Map<Integer, Integer> playersIds;
	private final GameStatus status;
	private final Long gameStart;
	private final Integer currentlyMovingTeam;
	
	/**
	 * @param gameStart game start time (millis from epoch)
	 */
	public GameResponseDto(Map<Integer, Integer> playersIds, GameStatus status, Long gameStart,
			Integer currentlyMovingTeam) {
		this.playersIds = playersIds;
		this.status = status;
		this.gameStart = gameStart;
		this.currentlyMovingTeam = currentlyMovingTeam;
	}

	public Map<Integer, Integer> getPlayersIds() {
		return playersIds;
	}

	public GameStatus getStatus() {
		return status;
	}

	public Long getGameStart() {
		return gameStart;
	}
	
	public Integer getCurrentlyMovingTeam() {
		return currentlyMovingTeam;
	}
	
	public abstract Game getGame();
}
