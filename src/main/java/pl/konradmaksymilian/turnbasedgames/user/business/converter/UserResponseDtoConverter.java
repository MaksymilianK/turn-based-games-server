package pl.konradmaksymilian.turnbasedgames.user.business.converter;

import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.core.converter.ModelToDtoConverter;
import pl.konradmaksymilian.turnbasedgames.user.data.entity.User;
import pl.konradmaksymilian.turnbasedgames.user.web.dto.UserResponseDto;

@Component
public class UserResponseDtoConverter implements ModelToDtoConverter<User, UserResponseDto> {

	@Override
	public UserResponseDto convert(User entity) {
		return new UserResponseDto(entity.getId(), entity.getNick(), entity.getRole());
	}
}
