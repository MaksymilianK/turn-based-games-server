package pl.konradmaksymilian.turnbasedgames.game.core.dto.event;

import pl.konradmaksymilian.turnbasedgames.game.core.action.SharedGameActionName;

public final class GameCountdownStartEventDto extends GameEventDto {
		
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public GameCountdownStartEventDto(long time) {
		super(time);
	}
	
	@Override
	public int getCode() {
		return SharedGameActionName.GAME_START.code();
	}
}
