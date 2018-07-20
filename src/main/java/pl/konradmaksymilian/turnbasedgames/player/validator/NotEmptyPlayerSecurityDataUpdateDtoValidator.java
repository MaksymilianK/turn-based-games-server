package pl.konradmaksymilian.turnbasedgames.player.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerSecurityDataUpdateDto;

public class NotEmptyPlayerSecurityDataUpdateDtoValidator 
		implements ConstraintValidator<NotEmptyPlayerSecurityDataUpdateDto, PlayerSecurityDataUpdateDto> {
	
	@Override
	public boolean isValid(PlayerSecurityDataUpdateDto dto, ConstraintValidatorContext context) {
		if (dto == null) {
			return true;
		} else {
			return dto.getNewNick() != null || dto.getNewEmail() != null || dto.getNewPassword() != null;
		}
	}
}
