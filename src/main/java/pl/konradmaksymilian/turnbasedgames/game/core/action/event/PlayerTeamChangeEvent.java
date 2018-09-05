package pl.konradmaksymilian.turnbasedgames.game.core.action.event;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.SharedGameAction;

public class PlayerTeamChangeEvent extends GameEvent {
	
	private final int previousTeam;
	
	private final int newTeam;
	
	public PlayerTeamChangeEvent(int previousTeam, int newTeam) {
		this.previousTeam = previousTeam;
		this.newTeam = newTeam;
	}
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PlayerTeamChangeEvent(long time, int previousTeam, int newTeam) {
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
		return SharedGameAction.GAME_START.code();
	}
}
