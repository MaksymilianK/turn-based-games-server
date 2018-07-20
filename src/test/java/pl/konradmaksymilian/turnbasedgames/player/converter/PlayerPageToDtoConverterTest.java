package pl.konradmaksymilian.turnbasedgames.player.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import pl.konradmaksymilian.turnbasedgames.player.PlayerTestUtils;
import pl.konradmaksymilian.turnbasedgames.player.Role;
import pl.konradmaksymilian.turnbasedgames.player.entity.Player;

public class PlayerPageToDtoConverterTest {

	private PlayerPageToDtoConverter playerPageToDtoConverter;
	
	@Mock
	private PlayerToDtoConverter playerToDtoConverter;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		playerPageToDtoConverter = new PlayerPageToDtoConverter(playerToDtoConverter);
	}
	
	@Test
	public void convert_returnsPageResponseDto() {
		var player = PlayerTestUtils.mockPlayer(5, Role.PLAYER);
		var players = new ArrayList<Player>();
		for (int i = 0; i < 25; i++) {
			players.add(player);
		}
		var sort = Sort.by("isAccountNonLocked").and(Sort.by("nick").descending());
		var page = new PageImpl<Player>(players, PageRequest.of(2, 25, sort), 100);
		when(playerToDtoConverter.convert(any())).thenReturn(PlayerTestUtils.mockPlayerResponseDto(player));
		
		var dto = playerPageToDtoConverter.convert(page);
		
		assertThat(dto.getPage()).isEqualTo(2);
		assertThat(dto.getSize()).isEqualTo(25);
		assertThat(dto.getTotalElements()).isEqualTo(100);
		assertThat(dto.getSort()).hasSize(2);
		assertThat(dto.getSort()).containsEntry("isAccountNonLocked", Sort.Direction.ASC);
		assertThat(dto.getSort()).containsEntry("nick", Sort.Direction.DESC);
		assertThat(dto.getContent()).hasSize(25);
		assertThat(dto.getContent()).allMatch(playerDto -> playerDto.getId() == 5);
	}
}
