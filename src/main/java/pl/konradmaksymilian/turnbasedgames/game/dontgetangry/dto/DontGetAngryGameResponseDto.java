package pl.konradmaksymilian.turnbasedgames.game.dontgetangry.dto;

import java.util.Map;

import pl.konradmaksymilian.turnbasedgames.core.exception.BuilderNullPropertyException;
import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameStatus;
import pl.konradmaksymilian.turnbasedgames.game.dto.PlayerTimeLimitedGameResponseDto;

public final class DontGetAngryGameResponseDto extends PlayerTimeLimitedGameResponseDto {

	private final Map<Integer, Map<Integer, Integer>> tokens;
	private final Integer lastDiceRolls;
	private final int maxPlayers;
		
	public DontGetAngryGameResponseDto(Map<Integer, Integer> playersIds, GameStatus status, Long gameStart,
			Integer currentlyMovingTeam, Integer playerTime, Map<Integer, Integer> playersTimes, Map<Integer,
			Map<Integer, Integer>> tokens, Integer lastDiceRolls, int maxPlayers) {
		super(playersIds, status, gameStart, playerTime, currentlyMovingTeam, playersTimes);
		this.tokens = tokens;
		this.lastDiceRolls = lastDiceRolls;
		this.maxPlayers = maxPlayers;
	}
	
	public Map<Integer, Map<Integer, Integer>> getTokens() {
		return tokens;
	}
	
	public Integer getLastDiceRolls() {
		return lastDiceRolls;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	@Override
	public Game getGame() {
		return Game.DONT_GET_ANGRY;
	}
	
	public static class Builder {
		
		private Map<Integer, Integer> playersIds;
		private GameStatus status;
		private Long gameStart;
		private Integer currentlyMovingTeam; 
		private Integer playerTime;
		private Map<Integer, Integer> playersTimes;
		private Map<Integer, Map<Integer, Integer>> tokens;
		private Integer lastDiceRolls;
		private Integer maxPlayers;
		
		public Builder playersIds(Map<Integer, Integer> playersIds) {
			this.playersIds = playersIds;
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
		
		public Builder playerTime(Integer playerTime) {
			this.playerTime = playerTime;
			return this;
		}
		
		public Builder playersTimes(Map<Integer, Integer> playersTimes) {
			this.playersTimes = playersTimes;
			return this;
		}
		
		public Builder tokens(Map<Integer, Map<Integer, Integer>> tokens) {
			this.tokens = tokens;
			return this;
		}
		
		public Builder lastDiceRolls(Integer lastDiceRolls) {
			this.lastDiceRolls = lastDiceRolls;
			return this;
		}
		
		public Builder maxPlayers(Integer maxPlayers) {
			this.maxPlayers = maxPlayers;
			return this;
		}
		
		public DontGetAngryGameResponseDto create() {
			if (playersIds == null || status == null || maxPlayers == null) {
				throw new BuilderNullPropertyException("Found null properties");
			}
			
			return new DontGetAngryGameResponseDto(playersIds, status, gameStart, currentlyMovingTeam, playerTime,
					playersTimes, tokens, lastDiceRolls, maxPlayers);
		}
	}
}
