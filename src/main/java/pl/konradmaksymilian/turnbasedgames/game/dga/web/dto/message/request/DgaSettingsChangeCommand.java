package pl.konradmaksymilian.turnbasedgames.game.dga.web.dto.message.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.core.data.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsRequestDto;
import pl.konradmaksymilian.turnbasedgames.game.dga.data.BoardSize;

public class DgaSettingsChangeCommand implements GameSettingsRequestDto {

	private final BoardSize boardSize;
	
	@Min(2)
	@Max(4)
	private final Integer maxPlayers;
	
	@Min(60)
	@Max(1800)
	private final Integer playerTime;
	
	public DgaSettingsChangeCommand(@JsonProperty("boardSize") BoardSize boardSize, 
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
