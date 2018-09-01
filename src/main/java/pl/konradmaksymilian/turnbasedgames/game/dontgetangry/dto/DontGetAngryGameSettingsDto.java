package pl.konradmaksymilian.turnbasedgames.game.dontgetangry.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.core.validator.DivisibleBy;
import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.GameSettingsDto;

public class DontGetAngryGameSettingsDto implements GameSettingsDto {

	@NotNull
	@Min(32)
	@Max(64)
	@DivisibleBy(8)
	private final Integer boardSize;
	
	@NotNull
	@Min(2)
	@Max(4)
	private final Integer maxPlayers;
	
	@NotNull
	@Min(60)
	@Max(1800)
	private final Integer playerTime;
	
	public DontGetAngryGameSettingsDto(@JsonProperty("boardSize") Integer boardSize, 
			@JsonProperty("maxPlayers") Integer maxPlayers, @JsonProperty("playerTime") Integer playerTime) {
		this.boardSize = boardSize;
		this.maxPlayers = maxPlayers;
		this.playerTime = playerTime;
	}

	public Integer getBoardSize() {
		return boardSize;
	}

	public Integer getMaxPlayers() {
		return maxPlayers;
	}

	public Integer getPlayerTime() {
		return playerTime;
	}

	@Override
	public Game getGame() {
		return Game.DONT_GET_ANGRY;
	}
}
