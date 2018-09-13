package pl.konradmaksymilian.turnbasedgames.game.core.action.event;

import java.time.Instant;

import pl.konradmaksymilian.turnbasedgames.game.core.action.CommonGameActionName;

public final class SimpleTokenMoveEvent extends MoveGameEvent {
	
	public SimpleTokenMoveEvent(int team) {
		super(team);
	}
	
	public SimpleTokenMoveEvent(Instant time, int team) {
		super(time, team);
	}
	
	@Override
	public int getCode() {
		return CommonGameActionName.TOKEN_MOVE.code();
	}
}
