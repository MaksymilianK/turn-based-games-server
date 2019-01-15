package pl.konradmaksymilian.turnbasedgames.game.core.web.dto;

import java.util.Map;

import pl.konradmaksymilian.turnbasedgames.core.exception.BuilderNullPropertyException;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.GameStatus;

public abstract class GameDetailsDto {

	private final GameSettingsResponseDto settings;
	private final GameStatusDetailsDto gameStatusDetails;
	private final Map<Integer, String> players;
	private final GameStatus status;
	private final Long gameStart;
	private final Integer currentlyMovingTeam;
	
	/**
	 * @param gameStart game start time (millis from epoch)
	 */
	public GameDetailsDto(GameSettingsResponseDto settings, GameStatusDetailsDto gameStatusDetails, 
			Map<Integer, String> players, GameStatus status, Long gameStart, Integer currentlyMovingTeam) {
		this.settings = settings;
		this.gameStatusDetails = gameStatusDetails;
		this.players = players;
		this.status = status;
		this.gameStart = gameStart;
		this.currentlyMovingTeam = currentlyMovingTeam;
	}
	
	public GameSettingsResponseDto getSettings() {
		return settings;
	}
	
	public GameStatusDetailsDto getGameStatusDetails() {
		return gameStatusDetails;
	}

	public Map<Integer, String> getPlayers() {
		return players;
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
	
	public static abstract class Builder {
		
		protected GameSettingsResponseDto settings;
		protected GameStatusDetailsDto gameStatusDetails;
		protected Map<Integer, String> players;
		protected GameStatus status;
		protected Long gameStart;
		protected Integer currentlyMovingTeam;
		
		public void throwExceptionIfNotBuilt() {
			if (settings == null || players == null || status == null) {
				throw new BuilderNullPropertyException(this.getClass());
			}
		}
	}
}
