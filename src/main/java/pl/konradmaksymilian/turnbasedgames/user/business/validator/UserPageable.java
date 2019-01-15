package pl.konradmaksymilian.turnbasedgames.user.business.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserPageableValidator.class)
public @interface UserPageable {

	String message() default "illegal user page request";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
