package pl.konradmaksymilian.turnbasedgames.game.core.dto;

import java.util.Map;

import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameStatus;

public abstract class GameResponseDto implements GameSettingsDto {

	private final Map<Integer, Integer> playersIds;
	private final GameStatus status;
	private final long gameStart;
	
	/**
	 * @param gameStart game start time (millis from epoch)
	 */
	public GameResponseDto(Map<Integer, Integer> playersIds, GameStatus status, long gameStart) {
		this.playersIds = playersIds;
		this.status = status;
		this.gameStart = gameStart;
	}

	public Map<Integer, Integer> getPlayersIds() {
		return playersIds;
	}

	public GameStatus getStatus() {
		return status;
	}

	public long getGameStart() {
		return gameStart;
	}
}
