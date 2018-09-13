package pl.konradmaksymilian.turnbasedgames.game.dto;

import java.util.Map;

import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameStatus;

public abstract class PlayerTimeLimitedGameResponseDto extends GameResponseDto {

	private final Integer playerTime;
	
	private final Map<Integer, Integer> playersTimes;
	
	/**
	 * @param gameStart game start time (millis from epoch)
	 */
	public PlayerTimeLimitedGameResponseDto(Map<Integer, Integer> playersIds, GameStatus status, Long gameStart,
			Integer playerTime, Integer currentlyMovingTeam, Map<Integer, Integer> playersTimes) {
		super(playersIds, status, gameStart, currentlyMovingTeam);
		this.playerTime = playerTime;
		this.playersTimes = playersTimes;
	}

	public Integer getPlayerTime() {
		return playerTime;
	}

	public Map<Integer, Integer> getPlayersTimes() {
		return playersTimes;
	}
}
