package pl.konradmaksymilian.turnbasedgames.game.core.dto.event;

import pl.konradmaksymilian.turnbasedgames.game.core.action.SharedGameActionName;

public final class PlayerTeamShiftEventDto extends GameEventDto {
	
	private final int previousTeam;
	
	private final int newTeam;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PlayerTeamShiftEventDto(long time, int previousTeam, int newTeam) {
		super(time);
		this.previousTeam = previousTeam;
		this.newTeam = newTeam;
	}

	public Integer getPreviousTeam() {
		return previousTeam;
	}

	public Integer getNewTeam() {
		return newTeam;
	}

	@Override
	public int getCode() {
		return SharedGameActionName.PLAYER_TEAM_SHIFT.code();
	}
}
