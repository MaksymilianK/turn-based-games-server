package pl.konradmaksymilian.turnbasedgames.player.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pl.konradmaksymilian.turnbasedgames.core.dto.PageResponseDto;
import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;
import pl.konradmaksymilian.turnbasedgames.core.exception.ResourceConflictException;
import pl.konradmaksymilian.turnbasedgames.core.exception.ResourceNotFoundException;
import pl.konradmaksymilian.turnbasedgames.player.Role;
import pl.konradmaksymilian.turnbasedgames.player.converter.PlayerPageToDtoConverter;
import pl.konradmaksymilian.turnbasedgames.player.converter.PlayerToDtoConverter;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerCreateDto;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerResponseDto;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerRoleUpdateDto;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerSecurityDataUpdateDto;
import pl.konradmaksymilian.turnbasedgames.player.entity.Player;
import pl.konradmaksymilian.turnbasedgames.player.repository.PlayerRepository;

@Service
public class PlayerServiceImpl implements PlayerService {
	
	private final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);
		
	private PlayerRepository playerRepository;
	
	private SecurityTokenProvider securityTokenProvider;
	
	private PlayerToDtoConverter playerToDtoConverter;
	
	private PlayerPageToDtoConverter playerPageToDtoConverter;
	
	private PasswordEncoder passwordEncoder;
	
	public PlayerServiceImpl(PlayerRepository playerRepository, SecurityTokenProvider securityTokenProvider,
			PlayerToDtoConverter playerToDtoConverter, PlayerPageToDtoConverter playerPageToDtoConverter,
			PasswordEncoder passwordEncoder) {
		this.playerRepository = playerRepository;
		this.securityTokenProvider = securityTokenProvider;
		this.playerToDtoConverter = playerToDtoConverter;
		this.playerPageToDtoConverter = playerPageToDtoConverter;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public Optional<Player> find(int id) {
		return playerRepository.findById(id);
	}
	
	@Override
	public Optional<Player> findCurrent() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			if (authentication.isAuthenticated()) {
				return Optional.of((Player) authentication.getPrincipal());
			}
		}
		return Optional.empty();
	}
	
	@Override
	public PlayerResponseDto get(int id) {
		return playerToDtoConverter.convert(getPlayerOrThrowException(id));
	}

	@Override
	public PlayerResponseDto getCurrent() {
		return playerToDtoConverter.convert(getCurrentOrThrowException());
	}
	
	@Override
	public PlayerResponseDto getByNick(String nick) {
		return playerToDtoConverter.convert(playerRepository.findByNick(nick).orElseThrow(
				() -> new ResourceNotFoundException(
						"Cannot find the player with the given nick: '" + nick + "'")));
	}
	
	@Override
	public PageResponseDto<PlayerResponseDto> getPage(Pageable pageable) {
		return playerPageToDtoConverter.convert(playerRepository.findAll(pageable));
	}
	
	@Override
	public Set<PlayerResponseDto> getAdministration() {
		return playerRepository.findAdministration().stream()
				.map(player -> playerToDtoConverter.convert(player))
				.collect(Collectors.toSet());
	}
	
	@Override
	public int create(PlayerCreateDto playerDto) {
		var nick = playerDto.getNick();
		var email = playerDto.getEmail();
		
		if (playerRepository.existsByNickIgnoreCase(nick.toLowerCase())) {
		    throw new ResourceConflictException("Player with the given nick: '" + nick + "' already exists in data base");
		} else if (playerRepository.existsByEmailIgnoreCase(email.toLowerCase())) {
		    throw new ResourceConflictException("Player with the given email: '" + email + "' already exists in data base");
		}
		
		int id = playerRepository.save(
				new Player(nick, email, passwordEncoder.encode(playerDto.getPassword()), Role.PLAYER)).getId();
		
		logger.info("Created a new player with id {}", id);
		
		return id;
	}
	
	@Override
	@PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
	public void updateRole(int id, PlayerRoleUpdateDto playerDto) {
		var current = findCurrent().get();
		var player = getPlayerOrThrowException(id);
		
		if (playerDto.getNewRole().equals(Role.HEAD_ADMINISTRATOR)) {
			changeHeadAdministrator(current, player);
		} else {
			changeRole(current, player, playerDto.getNewRole());
		}
	}
	
	@Override
	public void updateSecurityData(int id, PlayerSecurityDataUpdateDto playerDto) {
		var player = getPlayerOrThrowException(id);
		
		var token = securityTokenProvider.find(id).orElseThrow(() -> new AccessDeniedException(
				"Cannot find a security token for the player with the given id: " + id));
		if (!token.equals(playerDto.getSecurityToken())) {
			throw new AccessDeniedException("The sent security token does not match the saved token");
		}
		
		if (playerDto.getNewNick() != null) {
			player.setNick(playerDto.getNewNick());
		}
		
		if (playerDto.getNewEmail() != null) {
			player.setEmail(playerDto.getNewEmail());
		}
		
		if (playerDto.getNewPassword() != null) {
			player.setPassword(passwordEncoder.encode(playerDto.getNewPassword()));
		}
		
		securityTokenProvider.delete(id);
		playerRepository.save(player);
		
		logger.info("Updated security data of the player with the id '{}'", id);
	}
	
	@Override
	public void delete(int id) {
		var player = getPlayerOrThrowException(id);
		
		if (player.getRole().equals(Role.HEAD_ADMINISTRATOR)) {
			throw new BadOperationException("Cannot delete the head administrator's account");
		}
		
		playerRepository.deleteById(id);
		
		logger.warn("Deleted the player with id {}", id);
	}
	
	@Override
	public void createSecurityTokenForPlayer(String email) {
		playerRepository.findByEmail(email).ifPresent(player ->
				securityTokenProvider.generate(player.getId(), player.getEmail()));
	}
	
	private Player getCurrentOrThrowException() {
		return findCurrent().orElseThrow(
			() -> new ResourceNotFoundException("Cannot find the current player; the player is not logged in"));
	}
	
	private Player getPlayerOrThrowException(int id) {
		return playerRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Cannot find the player with the given id: " + id));
	}
	
	private void changeHeadAdministrator(Player current, Player player) {	
		if (!current.getRole().equals(Role.HEAD_ADMINISTRATOR)) {
			throw new AccessDeniedException(
					"Only the head administrator can change an another player's role to the head administrator");
		}
		
		player.setRole(Role.HEAD_ADMINISTRATOR);
		current.setRole(Role.ADMINISTRATOR);
		
		playerRepository.save(current);
		playerRepository.save(player);
		
		logger.warn("Changed the head administrator. The new head administrator is the player with id {}", player.getId());
	}
	
	private void changeRole(Player current, Player player, Role role) {
		player.setRole(role);
		
		playerRepository.save(player);
		
		logger.warn("Changed a role of the player with id {} from {} to {}. Changes done by the administrator with id {}",
				player.getId(), player.getRole(), role, current.getId());
	}
}
