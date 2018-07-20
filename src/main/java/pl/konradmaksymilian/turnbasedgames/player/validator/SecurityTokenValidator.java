package pl.konradmaksymilian.turnbasedgames.player.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SecurityTokenValidator implements ConstraintValidator<SecurityToken, String> {
	
	@Override
	public boolean isValid(String token, ConstraintValidatorContext context) {
		if (token == null) {
			return true;
		} else {
			if (token.length() != 17 ) {
				return false;
			}
			for (char character : token.toCharArray()) {
				if (character < 33 || character > 125) {
					return false;
				}
			}
			return true;
		}
	}
}