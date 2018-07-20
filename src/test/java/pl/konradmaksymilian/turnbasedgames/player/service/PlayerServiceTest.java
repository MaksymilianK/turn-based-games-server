package pl.konradmaksymilian.turnbasedgames.player.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;
import pl.konradmaksymilian.turnbasedgames.core.exception.ResourceConflictException;
import pl.konradmaksymilian.turnbasedgames.core.exception.ResourceNotFoundException;
import pl.konradmaksymilian.turnbasedgames.player.PlayerTestUtils;
import pl.konradmaksymilian.turnbasedgames.player.Role;
import pl.konradmaksymilian.turnbasedgames.player.converter.PlayerPageToDtoConverter;
import pl.konradmaksymilian.turnbasedgames.player.converter.PlayerToDtoConverter;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerCreateDto;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerResponseDto;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerRoleUpdateDto;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerSecurityDataUpdateDto;
import pl.konradmaksymilian.turnbasedgames.player.entity.Player;
import pl.konradmaksymilian.turnbasedgames.player.repository.PlayerRepository;

public class PlayerServiceTest {

	private PlayerService playerService;
	
	@Mock
	private PlayerRepository playerRepository;
	
	@Mock
	private SecurityTokenProvider securityTokenProvider;
	
	@Mock
	private PlayerToDtoConverter playerToDtoConverter;
	
	@Mock
	private PlayerPageToDtoConverter playerPageToDtoConverter;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private Authentication authentication;
	
	@Mock
	private SecurityContext securityContext;
		
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
				
		playerService = new PlayerServiceImpl(playerRepository, securityTokenProvider, playerToDtoConverter,
				playerPageToDtoConverter, passwordEncoder);
	}
	
	@Test
	public void findCurrent_givenCurrent_returnsCurrent() {
		var current = mockCurrent(Role.PLAYER);
		
		var player = playerService.findCurrent();
		
		assertThat(player).isPresent();
		assertThat(player.get()).isEqualTo(current);
	}
	
	@Test
	public void findCurrent_givenNoAuthentication_returnsNoCurrent() {
		when(securityContext.getAuthentication()).thenReturn(null);
		
		assertThat(playerService.findCurrent()).isEmpty();
	}
	
	@Test
	public void findCurrent_givenNoCurrent_returnsNoCurrent() {
		assertThat(playerService.findCurrent()).isEmpty();
	}
	
	@Test
	public void getByNick_givenExistingNick_returnsPlayerDto() {
		var player = PlayerTestUtils.mockPlayer(27, Role.PLAYER);
		when(playerRepository.findByNick("nick27")).thenReturn(Optional.of(player));
		var dto = PlayerTestUtils.mockPlayerResponseDto(player);
		when(playerToDtoConverter.convert(player)).thenReturn(dto);
		
		assertThat(playerService.getByNick("nick27")).isEqualTo(dto);
	}
	
	@Test
	public void getByNick_givenNonExistingNick_throwsException() {		
		assertThatExceptionOfType(ResourceNotFoundException.class)
				.isThrownBy(() -> playerService.getByNick("nick"))
				.withMessage("Cannot find the player with the given nick: 'nick'");
	}
	
	@Test
	public void getAdministration_returnsAdministration() {
		var helper = PlayerTestUtils.mockPlayer(2, Role.HELPER);
		var moderator = PlayerTestUtils.mockPlayer(3, Role.MODERATOR);
		var administration = new HashSet<Player>();
		administration.add(helper);
		administration.add(moderator);
		when(playerRepository.findAdministration()).thenReturn(administration);
		
		PlayerResponseDto helperDto = PlayerTestUtils.mockPlayerResponseDto(helper);
		PlayerResponseDto moderatorDto = PlayerTestUtils.mockPlayerResponseDto(moderator);
		when(playerToDtoConverter.convert(helper)).thenReturn(helperDto);
		when(playerToDtoConverter.convert(moderator)).thenReturn(moderatorDto);
		
		var administrationDto = playerService.getAdministration();
		
		assertThat(administrationDto).hasSize(2);
		assertThat(administrationDto).contains(helperDto);
		assertThat(administrationDto).contains(moderatorDto);
	}
	
	@Test
	public void create_givenCorrectDetails_createsNewPlayer() {
		var newPlayer = PlayerTestUtils.mockPlayer(6, Role.PLAYER);
		when(playerRepository.save(argThat(player -> player.getNick().equals("nick6")))).thenReturn(newPlayer);
		when(playerRepository.existsByNickIgnoreCase("nick")).thenReturn(false);
		when(playerRepository.existsByEmailIgnoreCase("email")).thenReturn(false);
		when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
		
		var playerDto = new PlayerCreateDto();
		playerDto.setNick("nick6");
		playerDto.setEmail("email");
		playerDto.setPassword("password");
		
		assertThat(playerService.create(playerDto)).isEqualTo(6);
	}
	
	@Test
	public void create_givenExistingNick_throwsException() {
		when(playerRepository.existsByNickIgnoreCase("nick")).thenReturn(true);
		when(playerRepository.existsByEmailIgnoreCase("email")).thenReturn(false);
		
		var playerDto = new PlayerCreateDto();
		playerDto.setNick("nIck");
		playerDto.setEmail("email");
		playerDto.setPassword("password");
		
		assertThatExceptionOfType(ResourceConflictException.class)
			.isThrownBy(() -> playerService.create(playerDto))
			.withMessage("Player with the given nick: 'nIck' already exists in data base");
	}
	
	@Test
	public void create_givenExistingEmail_throwsException() {
		when(playerRepository.existsByNickIgnoreCase("nick")).thenReturn(false);
		when(playerRepository.existsByEmailIgnoreCase("email")).thenReturn(true);
		
		var playerDto = new PlayerCreateDto();
		playerDto.setNick("nick");
		playerDto.setEmail("EmaiL");
		playerDto.setPassword("password");
		
		assertThatExceptionOfType(ResourceConflictException.class)
			.isThrownBy(() -> playerService.create(playerDto))
			.withMessage("Player with the given email: 'EmaiL' already exists in data base");
	}
	
	@Test 
	public void updateSecurityData_givenExistingPlayerEmail_updatesData() {
		var player = PlayerTestUtils.mockPlayer(7, Role.PLAYER);
		when(playerRepository.findById(7)).thenReturn(Optional.of(player));
		when(securityTokenProvider.find(7)).thenReturn(Optional.of("qwerty12345"));
		when(passwordEncoder.encode("newPassword")).thenReturn("hashedPassword");
		
		var playerDto = new PlayerSecurityDataUpdateDto();
		playerDto.setNewNick("newNick");
		playerDto.setNewEmail("newEmail");
		playerDto.setNewPassword("newPassword");
		playerDto.setSecurityToken("qwerty12345");
		
		playerService.updateSecurityData(7, playerDto);
		
		assertThat(player.getNick()).isEqualTo("newNick");
		assertThat(player.getEmail()).isEqualTo("newEmail");
		assertThat(player.getPassword()).isEqualTo("hashedPassword");
		assertThat(player.getRole()).isEqualTo(Role.PLAYER);
		verify(securityTokenProvider, times(1)).delete(7);
		verify(playerRepository, times(1)).save(player);
	}
	
	@Test
	public void updateSecurityData_givenNonExistingPlayerEmail_throwsException() {	
		assertThatExceptionOfType(ResourceNotFoundException.class)
				.isThrownBy(() -> playerService.updateSecurityData(5, new PlayerSecurityDataUpdateDto()))
				.withMessage("Cannot find the player with the given id: 5");
	}
	
	@Test
	public void updateSecurityData_givenNoSavedSecurityToken_throwsException() {
		when(playerRepository.findById(5)).thenReturn(Optional.of(PlayerTestUtils.mockPlayer(5, Role.PLAYER)));
		
		var playerDto = new PlayerSecurityDataUpdateDto();
		playerDto.setNewEmail("newEmail");
		playerDto.setSecurityToken("12345");

		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> playerService.updateSecurityData(5, playerDto))
				.withMessage("Cannot find a security token for the player with the given id: 5");
	}
	
	@Test
	public void updateSecurityData_givenSecurityTokensThatDoNotMatch_throwsException() {
		when(playerRepository.findById(5)).thenReturn(Optional.of(PlayerTestUtils.mockPlayer(5, Role.PLAYER)));
		when(securityTokenProvider.find(5)).thenReturn(Optional.of("qwerty12345"));
		
		var playerDto = new PlayerSecurityDataUpdateDto();
		playerDto.setNewPassword("newPassword");
		playerDto.setSecurityToken("23432k4k342");

		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> playerService.updateSecurityData(5, playerDto))
				.withMessage("The sent security token does not match the saved token");
	}
	
	@Test
	public void updateRole_givenHeadAdminAsCurrentAndNewHeadAdminRole_changesHeadAdmin() {
		var current = mockCurrent(Role.HEAD_ADMINISTRATOR);
		var player = PlayerTestUtils.mockPlayer(5, Role.PLAYER);
		when(playerRepository.findById(5)).thenReturn(Optional.of(player));
		
		var playerDto = new PlayerRoleUpdateDto();
		playerDto.setNewRole(Role.HEAD_ADMINISTRATOR);
		
		playerService.updateRole(5, playerDto);
				
		assertThat(player.getRole()).isEqualTo(Role.HEAD_ADMINISTRATOR);
		assertThat(current.getRole()).isEqualTo(Role.ADMINISTRATOR);
		verify(playerRepository, times(1)).save(player);
		verify(playerRepository, times(1)).save(current);
	}
	
	@Test
	public void updateRole_givenAdminAsCurrentAndNewHeadAdminRole_throwsException() {
		mockCurrent(Role.ADMINISTRATOR);
		when(playerRepository.findById(5)).thenReturn(Optional.of(PlayerTestUtils.mockPlayer(1, Role.PLAYER)));
		
		var playerDto = new PlayerRoleUpdateDto();
		playerDto.setNewRole(Role.HEAD_ADMINISTRATOR);
		
		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> playerService.updateRole(5, playerDto))
				.withMessage("Only the head administrator can change an another player's role to the head administrator");
	}
	
	@Test
	public void updateRole_givenAdminAsCurrentAndNewRole_updatesRole() {
		var current = mockCurrent(Role.ADMINISTRATOR);
		var player = PlayerTestUtils.mockPlayer(1, Role.PLAYER);
		when(playerRepository.findById(1)).thenReturn(Optional.of(player));
		
		var playerDto = new PlayerRoleUpdateDto();
		playerDto.setNewRole(Role.HELPER);
		
		playerService.updateRole(1, playerDto);
		
		assertThat(player.getRole()).isEqualTo(Role.HELPER);
		assertThat(current.getRole()).isEqualTo(Role.ADMINISTRATOR);
		verify(playerRepository, times(1)).save(player);
		verify(playerRepository, never()).save(current);
	}
	
	@Test
	public void delete_givenExistingId_deletesPlayer() {
		var player = PlayerTestUtils.mockPlayer(25, Role.PLAYER);
		when(playerRepository.findById(25)).thenReturn(Optional.of(player));
		
		playerService.delete(25);
		
		verify(playerRepository, times(1)).deleteById(25);
	}
	
	@Test
	public void delete_givenNonExistingId_throwsException() {		
		assertThatExceptionOfType(ResourceNotFoundException.class)
				.isThrownBy(() -> playerService.delete(6))
				.withMessage("Cannot find the player with the given id: 6");
	}
	
	@Test
	public void delete_givenHeadAdministratorId_throwsException() {
		when(playerRepository.findById(3)).thenReturn(Optional.of(PlayerTestUtils.mockPlayer(3, Role.HEAD_ADMINISTRATOR)));
		
		assertThatExceptionOfType(BadOperationException.class)
				.isThrownBy(() -> playerService.delete(3))
				.withMessage("Cannot delete the head administrator's account");
	}
	
	@Test
	public void createSecurityTokenForPlayer_givenExistingEmail_createsNewSecurityToken() {
		var player = PlayerTestUtils.mockPlayer(5, Role.PLAYER);
		when(playerRepository.findByEmail("email5")).thenReturn(Optional.of(player));
		
		playerService.createSecurityTokenForPlayer("email5");
		
		verify(securityTokenProvider, times(1)).generate(5, "email5");
	}
	
	@Test
	public void createSecurityTokenForPlayer_givenNonExistingEmail_doesNothing() {
		playerService.createSecurityTokenForPlayer("email5");
		
		verify(securityTokenProvider, never()).generate(anyInt(), anyString());
	}
	
	private Player mockCurrent(Role role) {
		var current = new Player("current", "", "", role);
		current.setId(26);
		
		when(authentication.isAuthenticated()).thenReturn(true);
		when(authentication.getPrincipal()).thenReturn(current);
		
		return current;
	}
}
