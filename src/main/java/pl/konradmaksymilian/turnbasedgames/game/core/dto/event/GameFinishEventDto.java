package pl.konradmaksymilian.turnbasedgames.game.core.dto.event;

import java.util.List;

import pl.konradmaksymilian.turnbasedgames.game.core.action.SharedGameActionName;

public class GameFinishEventDto extends GameEventDto {

	private final List<GameEventDto> gameHistory;

	/**
 	* @param time the message sending time (millis from epoch)
 	*/
	public GameFinishEventDto(long time, List<GameEventDto> gameHistory) {
		super(time);
		this.gameHistory = gameHistory;
	}

	@Override
	public int getCode() {
		return SharedGameActionName.GAME_FINISH.code();
	}
}
