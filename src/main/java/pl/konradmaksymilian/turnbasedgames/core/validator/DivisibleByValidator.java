package pl.konradmaksymilian.turnbasedgames.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DivisibleByValidator implements ConstraintValidator<DivisibleBy, Integer> {
	
	private int divisor;
	
	@Override
	public void initialize(DivisibleBy constraintAnnotation) {
		divisor = constraintAnnotation.value();
	}
	
	@Override
	public boolean isValid(Integer number, ConstraintValidatorContext context) {
		if (number == null) {
			return true;
		} else {
			return number % divisor == 0;
		}
	}
}