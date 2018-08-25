package pl.konradmaksymilian.turnbasedgames.user.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import pl.konradmaksymilian.turnbasedgames.user.dto.UserSecurityDataUpdateDto;

public class NotEmptyPlayerSecurityDataUpdateDtoValidator 
		implements ConstraintValidator<NotEmptyPlayerSecurityDataUpdateDto, UserSecurityDataUpdateDto> {
	
	@Override
	public boolean isValid(UserSecurityDataUpdateDto dto, ConstraintValidatorContext context) {
		if (dto == null) {
			return true;
		} else {
			return dto.getNewNick() != null || dto.getNewEmail() != null || dto.getNewPassword() != null;
		}
	}
}
