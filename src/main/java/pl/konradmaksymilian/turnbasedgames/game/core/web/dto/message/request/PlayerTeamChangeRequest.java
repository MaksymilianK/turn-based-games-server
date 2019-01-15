package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.SharedGameEventName;

public final class PlayerTeamChangeRequest extends NonHostOnlyGameRequest {
	
	@NotNull
	@PositiveOrZero
	private final Integer newTeam;
	
	public PlayerTeamChangeRequest(@JsonProperty("newTeam") Integer newTeam) {
		this.newTeam = newTeam;
	}

	public Integer getNewTeam() {
		return newTeam;
	}

	@Override
	public int getCode() {
		return SharedGameEventName.PLAYER_TEAM_CHANGE.code();
	}
}
