package pl.konradmaksymilian.turnbasedgames.game.core.data.event;

import java.time.Instant;

public final class GameStartEvent extends GameEvent {
	
	public GameStartEvent() {}
	
	public GameStartEvent(Instant time) {
		super(time);
	}
	
	@Override
	public int getCode() {
		return SharedGameEventName.GAME_START.code();
	}
}
