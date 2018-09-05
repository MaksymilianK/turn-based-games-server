package pl.konradmaksymilian.turnbasedgames.game.core.action.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.SharedGameAction;

public final class PlayerTeamChangeCommand extends NotHostGameCommand {
	
	@NotNull
	@PositiveOrZero
	private final Integer newTeam;
	
	public PlayerTeamChangeCommand(@JsonProperty("newTeam") Integer newTeam) {
		this.newTeam = newTeam;
	}

	public Integer getNewTeam() {
		return newTeam;
	}

	@Override
	public int getCode() {
		return SharedGameAction.PLAYER_TEAM_CHANGE.code();
	}
}
