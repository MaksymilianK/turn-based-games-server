package pl.konradmaksymilian.turnbasedgames.player.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.konradmaksymilian.turnbasedgames.player.PlayerTestUtils;
import pl.konradmaksymilian.turnbasedgames.player.Role;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerResponseDto;

public class PlayerToDtoConverterTest {
	
	private PlayerToDtoConverter playerToDtoConverter;
	
	@BeforeEach
	public void setUp() {
		playerToDtoConverter = new PlayerToDtoConverter();
	}
	
	@Test
	public void convert_returnsResponse() {
		var player = PlayerTestUtils.mockPlayer(6, Role.MODERATOR);
		
		PlayerResponseDto response = playerToDtoConverter.convert(player);
		
		assertThat(response.getId()).isEqualTo(6);
		assertThat(response.getNick()).isEqualTo("nick6");
		assertThat(response.getRole()).isEqualTo(Role.MODERATOR);
	}
}
