package pl.konradmaksymilian.turnbasedgames.game.dga.web.dto;

import pl.konradmaksymilian.turnbasedgames.game.core.data.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsChangeResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.dga.data.BoardSize;

public class DgaSettingsChangeResponseDto implements GameSettingsChangeResponseDto {

	private BoardSize boardSize;
	private int maxPlayers;
	private int playerTime;

	public BoardSize getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(BoardSize boardSize) {
		this.boardSize = boardSize;
	}
	
	public Integer getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	public Integer getPlayerTime() {
		return playerTime;
	}

	public void setPlayerTime(int playerTime) {
		this.playerTime = playerTime;
	}

	@Override
	public Game getGame() {
		return Game.DONT_GET_ANGRY;
	}
}
