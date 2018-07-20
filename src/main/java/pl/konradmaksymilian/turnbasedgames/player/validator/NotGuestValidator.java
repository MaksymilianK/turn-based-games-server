package pl.konradmaksymilian.turnbasedgames.player.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import pl.konradmaksymilian.turnbasedgames.player.Role;

public class NotGuestValidator implements ConstraintValidator<NotGuest, Role> {

	@Override
	public boolean isValid(Role role, ConstraintValidatorContext context) {
		if (role == null) {
			return true;
		} else {
			return !role.equals(Role.GUEST);
		}
	}
}
