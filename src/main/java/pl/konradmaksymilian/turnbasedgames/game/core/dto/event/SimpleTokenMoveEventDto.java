package pl.konradmaksymilian.turnbasedgames.game.core.dto.event;

import pl.konradmaksymilian.turnbasedgames.game.core.action.CommonGameActionName;

public final class SimpleTokenMoveEventDto extends GameEventDto {
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public SimpleTokenMoveEventDto(long time) {
		super(time);
	}
	
	@Override
	public int getCode() {
		return CommonGameActionName.TOKEN_MOVE.code();
	}
}
