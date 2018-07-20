package pl.konradmaksymilian.turnbasedgames.player.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotEmptyPlayerSecurityDataUpdateDtoValidator.class)
public @interface NotEmptyPlayerSecurityDataUpdateDto {

	String message() default "update request does not contain any new data";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
