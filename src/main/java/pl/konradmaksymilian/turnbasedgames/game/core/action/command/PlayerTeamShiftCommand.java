package pl.konradmaksymilian.turnbasedgames.game.core.action.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.SharedGameAction;

public final class PlayerTeamShiftCommand extends HostGameCommand {
	
	@NotNull
	@PositiveOrZero
	private final Integer firstTeam;
	
	@NotNull
	@PositiveOrZero
	private final Integer secondTeam;
	
	public PlayerTeamShiftCommand(@JsonProperty("firstTeam") Integer firstTeam,
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
		return SharedGameAction.PLAYER_TEAM_SHIFT.code();
	}
}
