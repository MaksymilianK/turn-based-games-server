package pl.konradmaksymilian.turnbasedgames.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

public class DoesNotStartWithIgnoreCaseValidator implements ConstraintValidator<DoesNotStartWithIgnoreCase, String> {
	
	private String prefix;
	
	@Override
	public void initialize(DoesNotStartWithIgnoreCase constraintAnnotation) {
		prefix = constraintAnnotation.value();
	}
	
	@Override
	public boolean isValid(String text, ConstraintValidatorContext context) {
		if (text == null) {
			return true;
		} else {
			return !StringUtils.startsWithIgnoreCase(text, prefix);
		}
	}
}