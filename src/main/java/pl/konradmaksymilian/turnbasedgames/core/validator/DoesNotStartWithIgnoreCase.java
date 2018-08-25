package pl.konradmaksymilian.turnbasedgames.core.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DoesNotStartWithIgnoreCaseValidator.class)
public @interface DoesNotStartWithIgnoreCase {
	
	String value();

	String message() default "starts with illegal sequence";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
