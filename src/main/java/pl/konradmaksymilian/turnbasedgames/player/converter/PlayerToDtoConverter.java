package pl.konradmaksymilian.turnbasedgames.player.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerResponseDto;
import pl.konradmaksymilian.turnbasedgames.player.entity.Player;

@Component
public class PlayerToDtoConverter implements Converter<Player, PlayerResponseDto> {

	@Override
	public PlayerResponseDto convert(Player source) {
		return new PlayerResponseDto(source.getId(), source.getNick(), source.getRole());
	}
}
