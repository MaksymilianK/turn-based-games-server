package pl.konradmaksymilian.turnbasedgames.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;
import pl.konradmaksymilian.turnbasedgames.core.exception.ResourceConflictException;
import pl.konradmaksymilian.turnbasedgames.core.exception.ResourceNotFoundException;
import pl.konradmaksymilian.turnbasedgames.user.Role;
import pl.konradmaksymilian.turnbasedgames.user.UserTestUtils;
import pl.konradmaksymilian.turnbasedgames.user.converter.UserToDtoConverter;
import pl.konradmaksymilian.turnbasedgames.user.dto.UserCreateDto;
import pl.konradmaksymilian.turnbasedgames.user.dto.UserResponseDto;
import pl.konradmaksymilian.turnbasedgames.user.dto.UserRoleUpdateDto;
import pl.konradmaksymilian.turnbasedgames.user.dto.UserSecurityDataUpdateDto;
import pl.konradmaksymilian.turnbasedgames.user.entity.User;
import pl.konradmaksymilian.turnbasedgames.user.repository.LoggedUserManager;
import pl.konradmaksymilian.turnbasedgames.user.repository.UserRepository;

public class UserServiceTest {

	private UserService userService;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private LoggedUserManager loggedUserManager;
	
	@Mock
	private SecurityTokenProvider securityTokenProvider;
	
	@Mock
	private UserToDtoConverter userToDtoConverter;
		
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
				
		userService = new UserServiceImpl(userRepository, loggedUserManager, securityTokenProvider, userToDtoConverter, 
				passwordEncoder);
	}
	
	@Test
	public void find_givenPositiveId_returnsRegisteredUser() {
		when(userRepository.findById(5)).thenReturn(Optional.of(UserTestUtils.mockUser(5)));
		
		var userOptional = userService.find(5);
		
		assertThat(userOptional).isPresent();
		var user = userOptional.get();
		assertThat(user.getId()).isEqualTo(5);
		assertThat(user.getNick()).isEqualTo("nick5");
		assertThat(user.getEmail()).isEqualTo("email5");
		assertThat(user.getPassword()).isEqualTo("password5");
		assertThat(user.getRole()).isEqualTo(Role.PLAYER);
	}
	
	@Test
	public void find_givenNegativeId_returnsGuest() {
		when(loggedUserManager.find(-5)).thenReturn(Optional.of(UserTestUtils.mockUser(5)));
		
		var userOptional = userService.find(-5);
		
		assertThat(userOptional).isPresent();
		var user = userOptional.get();
		assertThat(user.getId()).isEqualTo(5);
		assertThat(user.getNick()).isEqualTo("nick5");
		assertThat(user.getEmail()).isEqualTo("email5");
		assertThat(user.getPassword()).isEqualTo("password5");
		assertThat(user.getRole()).isEqualTo(Role.PLAYER);
	}
	
	@Test
	public void findCurrent_givenCurrent_returnsCurrent() {
		var current = mockCurrent(Role.PLAYER);
		
		var user = userService.findCurrent();
		
		assertThat(user).isPresent();
		assertThat(user.get()).isEqualTo(current);
	}
	
	@Test
	public void findCurrent_givenNoAuthentication_returnsNoCurrent() {
		when(securityContext.getAuthentication()).thenReturn(null);
		
		assertThat(userService.findCurrent()).isEmpty();
	}
	
	@Test
	public void findCurrent_givenAnonymousAuthentication_returnsNoCurrent() {
		var authentication = new AnonymousAuthenticationToken("anonymousUser", "AnonymousUser",
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_SOMTEHING")));
		when(securityContext.getAuthentication()).thenReturn(authentication);
		
		assertThat(userService.findCurrent()).isEmpty();
	}
	
	@Test
	public void findCurrent_givenNoCurrent_returnsNoCurrent() {
		assertThat(userService.findCurrent()).isEmpty();
	}
	
	@Test
	public void getByNick_givenExistingNick_returnsUserDto() {
		var user = UserTestUtils.mockUser(27);
		when(userRepository.findByNick("nick27")).thenReturn(Optional.of(user));
		var dto = UserTestUtils.mockUserResponseDto(user);
		when(userToDtoConverter.convert(user)).thenReturn(dto);
		
		assertThat(userService.getByNick("nick27")).isEqualTo(dto);
	}
	
	@Test
	public void getByNick_givenExistingGuestNick_returnsUserDto() {
		var guest = UserTestUtils.mockGuest(-27);
		when(loggedUserManager.findByNick("guest27")).thenReturn(Optional.of(guest));
		var dto = UserTestUtils.mockUserResponseDto(guest);
		when(userToDtoConverter.convert(guest)).thenReturn(dto);
		
		assertThat(userService.getByNick("guest27")).isEqualTo(dto);
	}
	
	@Test
	public void getByNick_givenNonExistingNick_throwsException() {		
		assertThatExceptionOfType(ResourceNotFoundException.class)
				.isThrownBy(() -> userService.getByNick("nick"))
				.withMessage("Cannot find the user with the given nick: 'nick'");
	}
	
	@Test
	public void getAdministration_returnsAdministration() {
		var helper = UserTestUtils.mockUser(2, Role.HELPER);
		var moderator = UserTestUtils.mockUser(3, Role.MODERATOR);
		var administration = new HashSet<User>();
		administration.add(helper);
		administration.add(moderator);
		when(userRepository.findAdministration()).thenReturn(administration);
		
		UserResponseDto helperDto = UserTestUtils.mockUserResponseDto(helper);
		UserResponseDto moderatorDto = UserTestUtils.mockUserResponseDto(moderator);
		when(userToDtoConverter.convert(helper)).thenReturn(helperDto);
		when(userToDtoConverter.convert(moderator)).thenReturn(moderatorDto);
		
		var administrationDto = userService.getAdministration();
		
		assertThat(administrationDto).hasSize(2);
		assertThat(administrationDto).contains(helperDto);
		assertThat(administrationDto).contains(moderatorDto);
	}
	
	@Test
	public void create_givenCorrectDetails_createsNewUser() {
		var newUser = UserTestUtils.mockUser(6);
		when(userRepository.save(argThat(user -> user.getNick().equals("nick6")))).thenReturn(newUser);
		when(userRepository.existsByNickIgnoreCase("nick")).thenReturn(false);
		when(userRepository.existsByEmailIgnoreCase("email")).thenReturn(false);
		when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
		
		var userDto = new UserCreateDto("nick6", "email", "password");
		
		assertThat(userService.create(userDto)).isEqualTo(6);
	}
	
	@Test
	public void create_givenExistingNick_throwsException() {
		when(userRepository.existsByNickIgnoreCase("nick")).thenReturn(true);
		when(userRepository.existsByEmailIgnoreCase("email")).thenReturn(false);
		
		var userDto = new UserCreateDto("nIck", "email", "password");
		
		assertThatExceptionOfType(ResourceConflictException.class)
			.isThrownBy(() -> userService.create(userDto))
			.withMessage("User with the given nick: 'nIck' already exists in data base");
	}
	
	@Test
	public void create_givenExistingEmail_throwsException() {
		when(userRepository.existsByNickIgnoreCase("nick")).thenReturn(false);
		when(userRepository.existsByEmailIgnoreCase("email")).thenReturn(true);
		
		var userDto = new UserCreateDto("nick", "EmaiL", "password");
		
		assertThatExceptionOfType(ResourceConflictException.class)
			.isThrownBy(() -> userService.create(userDto))
			.withMessage("User with the given email: 'EmaiL' already exists in data base");
	}
	
	@Test 
	public void updateSecurityData_givenExistingUserEmail_updatesData() {
		var user = UserTestUtils.mockUser(7);
		when(userRepository.findById(7)).thenReturn(Optional.of(user));
		when(securityTokenProvider.find(7)).thenReturn(Optional.of("qwerty12345"));
		when(passwordEncoder.encode("newPassword")).thenReturn("hashedPassword");
		
		var userDto = new UserSecurityDataUpdateDto();
		userDto.setNewNick("newNick");
		userDto.setNewEmail("newEmail");
		userDto.setNewPassword("newPassword");
		userDto.setSecurityToken("qwerty12345");
		
		userService.updateSecurityData(7, userDto);
		
		assertThat(user.getNick()).isEqualTo("newNick");
		assertThat(user.getEmail()).isEqualTo("newEmail");
		assertThat(user.getPassword()).isEqualTo("hashedPassword");
		assertThat(user.getRole()).isEqualTo(Role.PLAYER);
		verify(securityTokenProvider, times(1)).delete(7);
		verify(userRepository, times(1)).save(user);
	}
	
	@Test
	public void updateSecurityData_givenNonExistingUserEmail_throwsException() {	
		assertThatExceptionOfType(ResourceNotFoundException.class)
				.isThrownBy(() -> userService.updateSecurityData(5, new UserSecurityDataUpdateDto()))
				.withMessage("Cannot find the user with the given id: 5");
	}
	
	@Test
	public void updateSecurityData_givenNoSavedSecurityToken_throwsException() {
		when(userRepository.findById(5)).thenReturn(Optional.of(UserTestUtils.mockUser(5)));
		
		var userDto = new UserSecurityDataUpdateDto();
		userDto.setNewEmail("newEmail");
		userDto.setSecurityToken("12345");

		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> userService.updateSecurityData(5, userDto))
				.withMessage("Cannot find a security token for the user with the given id: 5");
	}
	
	@Test
	public void updateSecurityData_givenSecurityTokensThatDoNotMatch_throwsException() {
		when(userRepository.findById(5)).thenReturn(Optional.of(UserTestUtils.mockUser(5)));
		when(securityTokenProvider.find(5)).thenReturn(Optional.of("qwerty12345"));
		
		var userDto = new UserSecurityDataUpdateDto();
		userDto.setNewPassword("newPassword");
		userDto.setSecurityToken("23432k4k342");

		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> userService.updateSecurityData(5, userDto))
				.withMessage("The sent security token does not match the saved token");
	}
	
	@Test
	public void updateRole_givenHeadAdminAsCurrentAndNewHeadAdminRole_changesHeadAdmin() {
		var current = mockCurrent(Role.HEAD_ADMINISTRATOR);
		var user = UserTestUtils.mockUser(5);
		when(userRepository.findById(5)).thenReturn(Optional.of(user));
		
		var userDto = new UserRoleUpdateDto();
		userDto.setNewRole(Role.HEAD_ADMINISTRATOR);
		
		userService.updateRole(5, userDto);
				
		assertThat(user.getRole()).isEqualTo(Role.HEAD_ADMINISTRATOR);
		assertThat(current.getRole()).isEqualTo(Role.ADMINISTRATOR);
		verify(userRepository, times(1)).save(user);
		verify(userRepository, times(1)).save(current);
	}
	
	@Test
	public void updateRole_givenAdminAsCurrentAndNewHeadAdminRole_throwsException() {
		mockCurrent(Role.ADMINISTRATOR);
		when(userRepository.findById(5)).thenReturn(Optional.of(UserTestUtils.mockUser(1)));
		
		var userDto = new UserRoleUpdateDto();
		userDto.setNewRole(Role.HEAD_ADMINISTRATOR);
		
		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> userService.updateRole(5, userDto))
				.withMessage("Only the head administrator can change an another user's role to the head administrator");
	}
	
	@Test
	public void updateRole_givenAdminAsCurrentAndNewRole_updatesRole() {
		var current = mockCurrent(Role.ADMINISTRATOR);
		var user = UserTestUtils.mockUser(1);
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		
		var userDto = new UserRoleUpdateDto();
		userDto.setNewRole(Role.HELPER);
		
		userService.updateRole(1, userDto);
		
		assertThat(user.getRole()).isEqualTo(Role.HELPER);
		assertThat(current.getRole()).isEqualTo(Role.ADMINISTRATOR);
		verify(userRepository, times(1)).save(user);
		verify(userRepository, never()).save(current);
	}
	
	@Test
	public void delete_givenExistingId_deletesUser() {
		var user = UserTestUtils.mockUser(25);
		when(userRepository.findById(25)).thenReturn(Optional.of(user));
		
		userService.delete(25);
		
		verify(userRepository, times(1)).deleteById(25);
	}
	
	@Test
	public void delete_givenNonExistingId_throwsException() {		
		assertThatExceptionOfType(ResourceNotFoundException.class)
				.isThrownBy(() -> userService.delete(6))
				.withMessage("Cannot find the user with the given id: 6");
	}
	
	@Test
	public void delete_givenHeadAdministratorId_throwsException() {
		when(userRepository.findById(3)).thenReturn(Optional.of(UserTestUtils.mockUser(3, Role.HEAD_ADMINISTRATOR)));
		
		assertThatExceptionOfType(BadOperationException.class)
				.isThrownBy(() -> userService.delete(3))
				.withMessage("Cannot delete the head administrator's account");
	}
	
	@Test
	public void createSecurityTokenForUser_givenExistingEmail_createsNewSecurityToken() {
		var user = UserTestUtils.mockUser(5);
		when(userRepository.findByEmail("email5")).thenReturn(Optional.of(user));
		
		userService.createSecurityTokenForUser("email5");
		
		verify(securityTokenProvider, times(1)).generate(5, "email5");
	}
	
	@Test
	public void createSecurityTokenForUser_givenNonExistingEmail_doesNothing() {
		userService.createSecurityTokenForUser("email5");
		
		verify(securityTokenProvider, never()).generate(anyInt(), anyString());
	}
	
	private User mockCurrent(Role role) {
		var current = new User("current", "", "", role);
		current.setId(26);
		
		when(authentication.isAuthenticated()).thenReturn(true);
		when(authentication.getPrincipal()).thenReturn(current);
		
		return current;
	}
}
