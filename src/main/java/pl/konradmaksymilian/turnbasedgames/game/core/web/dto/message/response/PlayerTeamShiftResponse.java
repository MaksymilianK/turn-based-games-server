package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.SharedGameEventName;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomResponse;

public final class PlayerTeamShiftResponse extends RoomResponse {
	
	private final int previousTeam;
	private final int newTeam;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PlayerTeamShiftResponse(int previousTeam, int newTeam, long time) {
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
		return SharedGameEventName.PLAYER_TEAM_SHIFT.code();
	}
}
