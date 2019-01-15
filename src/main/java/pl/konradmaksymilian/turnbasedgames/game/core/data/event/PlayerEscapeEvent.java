package pl.konradmaksymilian.turnbasedgames.game.core.data.event;

import java.time.Instant;

public class PlayerEscapeEvent extends MoveGameEvent {
	
	public PlayerEscapeEvent(int team) {
		super(team);
	}
	
	public PlayerEscapeEvent(Instant time, int team) {
		super(time, team);
		
	}

	@Override
	public int getCode() {
		return SharedGameEventName.PLAYER_ESCAPE.code();
	}
}
