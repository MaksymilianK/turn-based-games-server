package pl.konradmaksymilian.turnbasedgames.player.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import pl.konradmaksymilian.turnbasedgames.player.PlayerTestUtils;
import pl.konradmaksymilian.turnbasedgames.player.Role;
import pl.konradmaksymilian.turnbasedgames.player.repository.PlayerRepository;

public class UserDetailsServiceImplTest {

	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Mock
	private PlayerRepository playerRepository;
		
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		userDetailsServiceImpl = new UserDetailsServiceImpl(playerRepository);
	}
	
	@Test
	public void loadUserByUsername_givenExistingNick_returnsPlayer() {
		var player = PlayerTestUtils.mockPlayer(1, Role.PLAYER);
		when(playerRepository.findByNick("nick")).thenReturn(Optional.of(player));
		
		assertThat(userDetailsServiceImpl.loadUserByUsername("nick")).isEqualTo(player);
	}
	
	@Test
	public void loadUserByUsername_givenNonExistingNick_throwsException() {		
		when(playerRepository.findByNick("nick")).thenReturn(Optional.empty());
		
		assertThatExceptionOfType(UsernameNotFoundException.class)
				.isThrownBy(() -> userDetailsServiceImpl.loadUserByUsername("nick"))
				.withMessage("Cannot find the player with the given username: 'nick' in the data base");
	}
}
