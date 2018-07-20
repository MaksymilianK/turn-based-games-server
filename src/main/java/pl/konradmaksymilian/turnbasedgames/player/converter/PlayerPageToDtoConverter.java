package pl.konradmaksymilian.turnbasedgames.player.converter;

import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.core.converter.PageToDtoConverter;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerResponseDto;
import pl.konradmaksymilian.turnbasedgames.player.entity.Player;

@Component
public class PlayerPageToDtoConverter extends PageToDtoConverter<Player, PlayerResponseDto> {

	public PlayerPageToDtoConverter(PlayerToDtoConverter playerToDtoConverter) {
		super(playerToDtoConverter);
	}
}