package pl.konradmaksymilian.turnbasedgames.game.core.dto.event;

import pl.konradmaksymilian.turnbasedgames.game.core.action.SharedGameActionName;

public class PlayerTeamChangeEventDto extends GameEventDto {
	
	private final int previousTeam;
	
	private final int newTeam;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PlayerTeamChangeEventDto(long time, int previousTeam, int newTeam) {
		super(time);
		this.previousTeam = previousTeam;
		this.newTeam = newTeam;
	}
		
	public int getPreviousTeam() {
		return previousTeam;
	}

	public int getNewTeam() {
		return newTeam;
	}

	@Override
	public int getCode() {
		return SharedGameActionName.GAME_START.code();
	}
}
