package pl.konradmaksymilian.turnbasedgames.game.dontgetangry.action.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.core.action.command.NotHostGameCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.action.event.GameEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.CommonGameActionName;

public final class DontGetAngryMoveEvent extends GameEvent {

	private final int token;
	
	public DontGetAngryMoveEvent(int token) {
		this.token = token;
	}
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public DontGetAngryMoveEvent(long time, int token) {
		super(time);
		this.token = token;
	}
	
	public int getToken() {
		return token;
	}
	
	@Override
	public int getCode() {
		return CommonGameActionName.MOVE.code();
	}
}
