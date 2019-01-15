package pl.konradmaksymilian.turnbasedgames.game.core.data.event;

import java.time.Instant;

public final class IdentifiableTokenMoveEvent extends MoveGameEvent {
	
	private final int token;
	
	public IdentifiableTokenMoveEvent(int token, int team) {
		super(team);
		this.token = token;
	}
	
	public IdentifiableTokenMoveEvent(int token, Instant time, int team) {
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
