package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.SharedGameEventName;

public final class PlayerTeamShiftRequest extends HostOnlyGameRequest {
	
	@NotNull
	@PositiveOrZero
	private final Integer firstTeam;
	
	@NotNull
	@PositiveOrZero
	private final Integer secondTeam;
	
	public PlayerTeamShiftRequest(@JsonProperty("firstTeam") Integer firstTeam,
			@JsonProperty("secondTeam") Integer secondTeam) {
		this.firstTeam = firstTeam;
		this.secondTeam = secondTeam;
	}

	public Integer getFirstTeam() {
		return firstTeam;
	}

	public Integer getSecondTeam() {
		return secondTeam;
	}

	@Override
	public int getCode() {
		return SharedGameEventName.PLAYER_TEAM_SHIFT.code();
	}
}
