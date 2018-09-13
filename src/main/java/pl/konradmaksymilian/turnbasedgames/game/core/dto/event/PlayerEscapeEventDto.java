package pl.konradmaksymilian.turnbasedgames.game.core.dto.event;

import pl.konradmaksymilian.turnbasedgames.game.core.action.CommonGameActionName;

public class PlayerEscapeEventDto extends GameEventDto {

	private final int team;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PlayerEscapeEventDto(long time, int team) {
		super(time);
		this.team = team;
		
	}
	
	public int getTeam() {
		return team;
	}

	@Override
	public int getCode() {
		return CommonGameActionName.DICE_ROLL.code();
	}
}
