package pl.konradmaksymilian.turnbasedgames.game.core.web.dto;

import java.util.Map;

import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.GameStatus;

public final class PlayerTimeLimitedGameDetailsDto extends GameDetailsDto {
	
	private final Map<Integer, Integer> playersTimes;
	
	/**
	 * @param gameStart game start time (millis from epoch)
	 */
	private PlayerTimeLimitedGameDetailsDto(GameSettingsResponseDto settings, GameStatusDetailsDto gameStatusDetails,
			Map<Integer, String> players, GameStatus status, Long gameStart, Integer currentlyMovingTeam, 
			Map<Integer, Integer> playersTimes) {
		super(settings, gameStatusDetails, players, status, gameStart, currentlyMovingTeam);
		this.playersTimes = playersTimes;
	}

	public Map<Integer, Integer> getPlayersTimes() {
		return playersTimes;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends GameDetailsDto.Builder {
		
		private Map<Integer, Integer> playersTimes;
		
		Builder() {}

		public Builder playersTimes(Map<Integer, Integer> playersTimes) {
			this.playersTimes = playersTimes;
			return this;
		}

		public Builder settings(GameSettingsResponseDto settings) {
			this.settings = settings;
			return this;
		}

		public Builder gameStatusDetails(GameStatusDetailsDto gameStatusDetails) {
			this.gameStatusDetails = gameStatusDetails;
			return this;
		}

		public Builder players(Map<Integer, String> players) {
			this.players = players;
			return this;
		}

		public Builder status(GameStatus status) {
			this.status = status;
			return this;
		}

		public Builder gameStart(Long gameStart) {
			this.gameStart = gameStart;
			return this;
		}

		public Builder currentlyMovingTeam(Integer currentlyMovingTeam) {
			this.currentlyMovingTeam = currentlyMovingTeam;
			return this;
		}
		
		public PlayerTimeLimitedGameDetailsDto build() {
			throwExceptionIfNotBuilt();
			return new PlayerTimeLimitedGameDetailsDto(settings, gameStatusDetails, players, status, gameStart,
					currentlyMovingTeam, playersTimes);
		}
	}
}
