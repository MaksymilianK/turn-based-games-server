package pl.konradmaksymilian.turnbasedgames.game.dga.web.dto;

import pl.konradmaksymilian.turnbasedgames.game.core.data.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.dga.data.BoardSize;

public class DgaSettingsResponseDto implements GameSettingsResponseDto {

	private final BoardSize boardSize;
	private final int maxPlayers;
	private final int playerTime;
	
	public DgaSettingsResponseDto(BoardSize boardSize, int maxPlayers, int playerTime) {
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
