package pl.konradmaksymilian.turnbasedgames.game.core.dto.event;

import pl.konradmaksymilian.turnbasedgames.game.core.action.CommonGameActionName;

public class DiceRollEventDto extends GameEventDto {

	private final int rolledValue;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public DiceRollEventDto(long time, int rolledValue) {
		super(time);
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
