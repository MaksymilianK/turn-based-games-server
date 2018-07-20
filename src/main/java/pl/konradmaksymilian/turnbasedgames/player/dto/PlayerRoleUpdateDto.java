package pl.konradmaksymilian.turnbasedgames.player.dto;

import javax.validation.constraints.NotNull;

import pl.konradmaksymilian.turnbasedgames.player.Role;
import pl.konradmaksymilian.turnbasedgames.player.validator.NotGuest;

public class PlayerRoleUpdateDto {

	@NotNull
	@NotGuest
	private Role newRole;

	public Role getNewRole() {
		return newRole;
	}

	public void setNewRole(Role newRole) {
		this.newRole = newRole;
	}
}
