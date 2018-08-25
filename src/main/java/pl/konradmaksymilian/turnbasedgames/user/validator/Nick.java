package pl.konradmaksymilian.turnbasedgames.user.validator;

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
@Constraint(validatedBy = NickValidator.class)
public @interface Nick {

	String message() default "illegal nick";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
