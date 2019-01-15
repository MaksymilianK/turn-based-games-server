package pl.konradmaksymilian.turnbasedgames.user.business.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

public class NickValidator implements ConstraintValidator<Nick, String> {
	
	@Override
	public boolean isValid(String nick, ConstraintValidatorContext context) {
		if (nick == null) {
			return true;
		} else if (nick.length() > 1 && nick.length() < 16) {
			if (nick.chars().allMatch(character -> Character.isAlphabetic(character) || Character.isDigit(character))) {
				return !StringUtils.startsWithIgnoreCase(nick, "headadmin") 
						&& !StringUtils.startsWithIgnoreCase(nick, "admin") 
						&& !StringUtils.startsWithIgnoreCase(nick, "moderator") 
						&& !StringUtils.startsWithIgnoreCase(nick, "helper");
			}
		}
		return false;
	}
}