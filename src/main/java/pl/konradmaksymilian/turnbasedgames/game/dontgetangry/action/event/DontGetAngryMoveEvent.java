package pl.konradmaksymilian.turnbasedgames.game.dontgetangry.action.event;

import java.time.Instant;

import pl.konradmaksymilian.turnbasedgames.game.core.action.CommonGameActionName;
import pl.konradmaksymilian.turnbasedgames.game.core.action.event.MoveGameEvent;

public final class DontGetAngryMoveEvent extends MoveGameEvent {

	private final int token;
	
	public DontGetAngryMoveEvent(int team, int token) {
		super(team);
		this.token = token;
	}
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public DontGetAngryMoveEvent(Instant time, int team, int token) {
		super(time, team);
		this.token = token;
	}
	
	public int getToken() {
		return token;
	}
	
	@Override
	public int getCode() {
		return CommonGameActionName.TOKEN_MOVE.code();
	}
}
