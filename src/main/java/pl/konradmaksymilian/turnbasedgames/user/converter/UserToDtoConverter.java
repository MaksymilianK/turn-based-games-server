package pl.konradmaksymilian.turnbasedgames.user.converter;

import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.core.converter.EntityToDtoConverter;
import pl.konradmaksymilian.turnbasedgames.user.dto.UserResponseDto;
import pl.konradmaksymilian.turnbasedgames.user.entity.User;

@Component
public class UserToDtoConverter implements EntityToDtoConverter<User, UserResponseDto> {

	@Override
	public UserResponseDto convert(User entity) {
		return new UserResponseDto(entity.getId(), entity.getNick(), entity.getRole());
	}
}
