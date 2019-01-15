package pl.konradmaksymilian.turnbasedgames.user.business.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pl.konradmaksymilian.turnbasedgames.core.dto.PageResponseDto;
import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;
import pl.konradmaksymilian.turnbasedgames.core.exception.ResourceConflictException;
import pl.konradmaksymilian.turnbasedgames.core.exception.ResourceNotFoundException;
import pl.konradmaksymilian.turnbasedgames.user.business.converter.UserResponseDtoConverter;
import pl.konradmaksymilian.turnbasedgames.user.data.Role;
import pl.konradmaksymilian.turnbasedgames.user.data.entity.User;
import pl.konradmaksymilian.turnbasedgames.user.data.repository.LoggedUserManager;
import pl.konradmaksymilian.turnbasedgames.user.data.repository.UserRepository;
import pl.konradmaksymilian.turnbasedgames.user.web.dto.UserCreateDto;
import pl.konradmaksymilian.turnbasedgames.user.web.dto.UserResponseDto;
import pl.konradmaksymilian.turnbasedgames.user.web.dto.UserRoleUpdateDto;
import pl.konradmaksymilian.turnbasedgames.user.web.dto.UserSecurityDataUpdateDto;
import pl.konradmaksymilian.turnbasedgames.user.web.dto.UsersStatsResponseDto;

@Service
public class UserServiceImpl implements UserService {
	
	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
		
	private UserRepository userRepository;
	
	private LoggedUserManager loggedUserManager;
	
	private SecurityTokenProvider securityTokenProvider;
	
	private UserResponseDtoConverter userToDtoConverter;
		
	private PasswordEncoder passwordEncoder;
	
	public UserServiceImpl(UserRepository userRepository, LoggedUserManager loggedUserManager,
			SecurityTokenProvider securityTokenProvider, UserResponseDtoConverter userToDtoConverter,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.loggedUserManager = loggedUserManager;
		this.securityTokenProvider = securityTokenProvider;
		this.userToDtoConverter = userToDtoConverter;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public UsersStatsResponseDto getStats() {
		return new UsersStatsResponseDto((int) userRepository.count(), loggedUserManager.count());
	}
	
	@Override
	public Optional<User> find(int id) {
		if (id >= 0) {
			return userRepository.findById(id);
		} else {
			return loggedUserManager.find(id);
		}
	}
	
	@Override
	public Optional<User> findCurrent() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			if (!(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
				return Optional.of((User) authentication.getPrincipal());
			}
		}
		return Optional.empty();
	}
	
	@Override
	public UserResponseDto get(int id) {
		return userToDtoConverter.convert(getUserOrThrowException(id));
	}

	@Override
	public UserResponseDto getCurrent() {
		var current = getCurrentOrThrowException();
		if (current.getRole().equals(Role.GUEST)) {
			throw new ResourceNotFoundException("The current user is a guest");
		} else {
			return userToDtoConverter.convert(current);
		}
	}
	
	@Override
	public UserResponseDto getByNick(String nick) {
		Optional<User> user;
		if (nick.startsWith("guest")) {
			user = loggedUserManager.findByNick(nick);
		} else {
			user = userRepository.findByNick(nick);
		}
		return userToDtoConverter.convert(user.orElseThrow(
				() -> new ResourceNotFoundException(
						"Cannot find the user with the given nick: '" + nick + "'")));
	}
	
	@Override
	public PageResponseDto<UserResponseDto> getPage(Pageable pageable) {
		return userToDtoConverter.convertPage(userRepository.findAll(pageable));
	}
	
	@Override
	public Set<UserResponseDto> getAdministration() {
		return userRepository.findAdministration().stream()
				.map(user -> userToDtoConverter.convert(user))
				.collect(Collectors.toSet());
	}
	
	@Override
	public int create(UserCreateDto userDto) {
		var nick = userDto.getNick();
		var email = userDto.getEmail();
		
		if (userRepository.existsByNickIgnoreCase(nick.toLowerCase())) {
		    throw new ResourceConflictException("User with the given nick: '" + nick + "' already exists in data base");
		} else if (userRepository.existsByEmailIgnoreCase(email.toLowerCase())) {
		    throw new ResourceConflictException("User with the given email: '" + email + "' already exists in data base");
		}
		
		int id = userRepository.save(
				new User(nick, email, passwordEncoder.encode(userDto.getPassword()), Role.PLAYER)).getId();
		
		logger.info("Created a new user with id {}", id);
		
		return id;
	}
	
	@Override
	public void updateRole(int id, UserRoleUpdateDto userDto) {
		var current = findCurrent().get();
		var user = getUserOrThrowException(id);
		
		if (userDto.getNewRole().equals(Role.HEAD_ADMINISTRATOR)) {
			changeHeadAdministrator(current, user);
		} else {
			changeRole(current, user, userDto.getNewRole());
		}
	}
	
	@Override
	public void updateSecurityData(int id, UserSecurityDataUpdateDto userDto) {
		var user = getUserOrThrowException(id);
		
		var token = securityTokenProvider.find(id).orElseThrow(() -> new AccessDeniedException(
				"Cannot find a security token for the user with the given id: " + id));
		if (!token.equals(userDto.getSecurityToken())) {
			throw new AccessDeniedException("The sent security token does not match the saved token");
		}
		
		if (userDto.getNewNick() != null) {
			user.setNick(userDto.getNewNick());
		}
		
		if (userDto.getNewEmail() != null) {
			user.setEmail(userDto.getNewEmail());
		}
		
		if (userDto.getNewPassword() != null) {
			user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
		}
		
		securityTokenProvider.delete(id);
		userRepository.save(user);
		
		logger.info("Updated security data of the user with the id '{}'", id);
	}
	
	@Override
	public void delete(int id) {
		var user = getUserOrThrowException(id);
		
		if (user.getRole().equals(Role.HEAD_ADMINISTRATOR)) {
			throw new BadOperationException("Cannot delete the head administrator's account");
		}
		
		userRepository.deleteById(id);
		
		logger.warn("Deleted the user with id {}", id);
	}
	
	@Override
	public void createSecurityTokenForUser(String email) {
		userRepository.findByEmail(email).ifPresent(user ->
				securityTokenProvider.generate(user.getId(), user.getEmail()));
	}
	
	private User getCurrentOrThrowException() {
		return findCurrent().orElseThrow(
			() -> new ResourceNotFoundException("Cannot find the current user; the user is not logged in"));
	}
	
	private User getUserOrThrowException(int id) {
		return userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Cannot find the user with the given id: " + id));
	}
	
	private void changeHeadAdministrator(User current, User user) {	
		if (!current.getRole().equals(Role.HEAD_ADMINISTRATOR)) {
			throw new AccessDeniedException(
					"Only the head administrator can change an another user's role to the head administrator");
		}
		
		user.setRole(Role.HEAD_ADMINISTRATOR);
		current.setRole(Role.ADMINISTRATOR);
		
		userRepository.save(current);
		userRepository.save(user);
		
		logger.warn("Changed the head administrator. The new head administrator is the user with id {}", user.getId());
	}
	
	private void changeRole(User current, User user, Role role) {
		user.setRole(role);
		
		userRepository.save(user);
		
		logger.warn("Changed a role of the user with id {} from {} to {}. Changes done by the administrator with id {}",
				user.getId(), user.getRole(), role, current.getId());
	}
}
