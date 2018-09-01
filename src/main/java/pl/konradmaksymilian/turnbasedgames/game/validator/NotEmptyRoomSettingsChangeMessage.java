package pl.konradmaksymilian.turnbasedgames.game.validator;

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
@Constraint(validatedBy = NotEmptyRoomSettingsChangeMessageValidator.class)
public @interface NotEmptyRoomSettingsChangeMessage {

	String message() default "empty dto";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
