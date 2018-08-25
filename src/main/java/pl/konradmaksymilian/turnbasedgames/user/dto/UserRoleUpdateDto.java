package pl.konradmaksymilian.turnbasedgames.user.dto;

import javax.validation.constraints.NotNull;

import pl.konradmaksymilian.turnbasedgames.user.Role;
import pl.konradmaksymilian.turnbasedgames.user.validator.NotGuest;

public class UserRoleUpdateDto {

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
