package pl.konradmaksymilian.turnbasedgames.game.core.action.event;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.SharedGameAction;

public final class PlayerTeamShiftEvent extends GameEvent {
	
	private final int previousTeam;
	
	private final int newTeam;
	
	public PlayerTeamShiftEvent(int previousTeam, int newTeam) {
		this.previousTeam = previousTeam;
		this.newTeam = newTeam;
	}
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PlayerTeamShiftEvent(long time, int previousTeam, int newTeam) {
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
		return SharedGameAction.PLAYER_TEAM_SHIFT.code();
	}
}
