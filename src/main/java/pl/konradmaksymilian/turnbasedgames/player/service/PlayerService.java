package pl.konradmaksymilian.turnbasedgames.player.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import pl.konradmaksymilian.turnbasedgames.core.dto.PageResponseDto;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerCreateDto;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerResponseDto;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerRoleUpdateDto;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerSecurityDataUpdateDto;
import pl.konradmaksymilian.turnbasedgames.player.entity.Player;

public interface PlayerService {

	Optional<Player> find(int id);
	
	Optional<Player> findCurrent();
	
	@PreAuthorize("isAuthenticated()")
	PlayerResponseDto get(int id);
	
	PlayerResponseDto getCurrent();
	
	@PreAuthorize("hasRole('PLAYER')")
	PlayerResponseDto getByNick(String nick);
	
	@PreAuthorize("hasRole('HELPER')")
	PageResponseDto<PlayerResponseDto> getPage(Pageable pageable);
	
	Set<PlayerResponseDto> getAdministration();
	
	@Transactional
    int create(PlayerCreateDto playerDto);
	
	void updateRole(int id, PlayerRoleUpdateDto playerDto);
	
	@Transactional
	void updateSecurityData(int id, PlayerSecurityDataUpdateDto playerDto);
	
	@PreAuthorize("hasRole('HEAD_ADMINISTRATOR')")
	@Transactional
	void delete(int id);
	
	@Transactional
	void createSecurityTokenForPlayer(String email);
}
