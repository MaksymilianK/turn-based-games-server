package pl.konradmaksymilian.turnbasedgames.game.dga.web.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.core.data.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsRequestDto;
import pl.konradmaksymilian.turnbasedgames.game.dga.data.BoardSize;

public class DgaSettingsRequestDto implements GameSettingsRequestDto {

	@NotNull
	private final BoardSize boardSize;
	
	@NotNull
	@Min(2)
	@Max(4)
	private final Integer maxPlayers;
	
	@NotNull
	@Min(60)
	@Max(1800)
	private final Integer playerTime;
	
	public DgaSettingsRequestDto(@JsonProperty("boardSize") BoardSize boardSize,
			@JsonProperty("maxPlayers") Integer maxPlayers, @JsonProperty("playerTime") Integer playerTime) {
		this.boardSize = boardSize;
		this.maxPlayers = maxPlayers;
		this.playerTime = playerTime;
	}

	public BoardSize getBoardSize() {
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
