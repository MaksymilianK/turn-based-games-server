package pl.konradmaksymilian.turnbasedgames.game.core.action.event;

import java.time.Instant;

import pl.konradmaksymilian.turnbasedgames.game.core.action.SharedGameActionName;

public final class GameStartEvent extends GameEvent {
	
	public GameStartEvent() {}
	
	public GameStartEvent(Instant time) {
		super(time);
	}
	
	@Override
	public int getCode() {
		return SharedGameActionName.GAME_START.code();
	}
}
