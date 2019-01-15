package pl.konradmaksymilian.turnbasedgames.game.dga.action.event;

import java.time.Instant;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.CommonGameEventName;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.MoveGameEvent;

public final class DgaMoveEvent extends MoveGameEvent {

	private final int token;
	
	public DgaMoveEvent(int team, int token) {
		super(team);
		this.token = token;
	}
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public DgaMoveEvent(Instant time, int team, int token) {
		super(time, team);
		this.token = token;
	}
	
	public int getToken() {
		return token;
	}
	
	@Override
	public int getCode() {
		return CommonGameEventName.TOKEN_MOVE.code();
	}
}
