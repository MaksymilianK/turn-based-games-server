package pl.konradmaksymilian.turnbasedgames.game.core.action.event;

import java.time.Instant;

import pl.konradmaksymilian.turnbasedgames.game.core.action.CommonGameActionName;

public class DiceRollEvent extends MoveGameEvent {

	private final int rolledValue;
	
	public DiceRollEvent(int team, int rolledValue) {
		super(team);
		this.rolledValue = rolledValue;
	}
	
	public DiceRollEvent(Instant time, int team, int rolledValue) {
		super(time, team);
		this.rolledValue = rolledValue;
		
	}
	
	public int getRolledValue() {
		return rolledValue;
	}

	@Override
	public int getCode() {
		return CommonGameActionName.DICE_ROLL.code();
	}
}
