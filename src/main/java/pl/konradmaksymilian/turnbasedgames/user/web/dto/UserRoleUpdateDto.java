package pl.konradmaksymilian.turnbasedgames.user.web.dto;

import javax.validation.constraints.NotNull;

import pl.konradmaksymilian.turnbasedgames.user.business.validator.NotGuest;
import pl.konradmaksymilian.turnbasedgames.user.data.Role;

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
