package pl.konradmaksymilian.turnbasedgames.game.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.RoomSettingsChangeMessage;

public class NotEmptyRoomSettingsChangeMessageValidator 
		implements ConstraintValidator<NotEmptyRoomSettingsChangeMessage, RoomSettingsChangeMessage> {

	@Override
	public boolean isValid(RoomSettingsChangeMessage message, ConstraintValidatorContext context) {
		return message.areObserversAllowed() != null || message.getPrivacy() != null || message.getChatPolicy() != null;
	}
}
