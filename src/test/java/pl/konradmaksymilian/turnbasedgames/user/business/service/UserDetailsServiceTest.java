package pl.konradmaksymilian.turnbasedgames.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import pl.konradmaksymilian.turnbasedgames.user.UserTestUtils;
import pl.konradmaksymilian.turnbasedgames.user.Role;
import pl.konradmaksymilian.turnbasedgames.user.repository.UserRepository;
import pl.konradmaksymilian.turnbasedgames.user.service.UserDetailsServiceImpl;

public class UserDetailsServiceTest {

	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Mock
	private UserRepository userRepository;
		
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		userDetailsServiceImpl = new UserDetailsServiceImpl(userRepository);
	}
	
	@Test
	public void loadUserByUsername_givenExistingNick_returnsUser() {
		var user = UserTestUtils.mockUser(1, Role.PLAYER);
		when(userRepository.findByNick("nick")).thenReturn(Optional.of(user));
		
		assertThat(userDetailsServiceImpl.loadUserByUsername("nick")).isEqualTo(user);
	}
	
	@Test
	public void loadUserByUsername_givenNonExistingNick_throwsException() {		
		when(userRepository.findByNick("nick")).thenReturn(Optional.empty());
		
		assertThatExceptionOfType(UsernameNotFoundException.class)
				.isThrownBy(() -> userDetailsServiceImpl.loadUserByUsername("nick"))
				.withMessage("Cannot find the user with the given username: 'nick' in the data base");
	}
}
