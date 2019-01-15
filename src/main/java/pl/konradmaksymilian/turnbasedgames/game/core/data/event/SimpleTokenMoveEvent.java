package pl.konradmaksymilian.turnbasedgames.game.core.data.event;

import java.time.Instant;

public final class SimpleTokenMoveEvent extends MoveGameEvent {
	
	public SimpleTokenMoveEvent(int team) {
		super(team);
	}
	
	public SimpleTokenMoveEvent(Instant time, int team) {
		super(time, team);
	}
	
	@Override
	public int getCode() {
		return CommonGameEventName.TOKEN_MOVE.code();
	}
}
