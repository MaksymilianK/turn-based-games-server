package pl.konradmaksymilian.turnbasedgames.game.dontgetangry.action.command;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.core.validator.DivisibleBy;
import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.GameSettingsDto;

public class DontGetAngryGameSettingsChangeCommand implements GameSettingsDto {

	@Min(32)
	@Max(64)
	@DivisibleBy(8)
	private final Integer boardSize;
	
	@Min(2)
	@Max(4)
	private final Integer maxPlayers;
	
	@Min(60)
	@Max(1800)
	private final Integer playerTime;
	
	public DontGetAngryGameSettingsChangeCommand(@JsonProperty("boardSize") Integer boardSize, 
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
