package pl.konradmaksymilian.turnbasedgames.player.validator;

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
@Constraint(validatedBy = NotGuestValidator.class)
public @interface NotGuest {

	String message() default "cannot be GUEST";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
