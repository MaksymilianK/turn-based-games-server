package pl.konradmaksymilian.turnbasedgames.game.core.action.event;

import java.time.Instant;

import pl.konradmaksymilian.turnbasedgames.game.core.action.SharedGameActionName;

public class PlayerEscapeEvent extends MoveGameEvent {
	
	public PlayerEscapeEvent(int team) {
		super(team);
	}
	
	public PlayerEscapeEvent(Instant time, int team) {
		super(time, team);
		
	}

	@Override
	public int getCode() {
		return SharedGameActionName.PLAYER_ESCAPE.code();
	}
}
