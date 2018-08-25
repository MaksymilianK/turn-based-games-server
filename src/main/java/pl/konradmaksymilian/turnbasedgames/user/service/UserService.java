package pl.konradmaksymilian.turnbasedgames.user.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import pl.konradmaksymilian.turnbasedgames.core.dto.PageResponseDto;
import pl.konradmaksymilian.turnbasedgames.user.dto.UserCreateDto;
import pl.konradmaksymilian.turnbasedgames.user.dto.UserResponseDto;
import pl.konradmaksymilian.turnbasedgames.user.dto.UserRoleUpdateDto;
import pl.konradmaksymilian.turnbasedgames.user.dto.UserSecurityDataUpdateDto;
import pl.konradmaksymilian.turnbasedgames.user.entity.User;

@Transactional(readOnly = true)
public interface UserService {

	Optional<User> find(int id);
	
	Optional<User> findCurrent();
	
	@PreAuthorize("isAuthenticated()")
	UserResponseDto get(int id);
	
	UserResponseDto getCurrent();
	
	@PreAuthorize("hasRole('PLAYER')")
	UserResponseDto getByNick(String nick);
	
	@PreAuthorize("hasRole('HELPER')")
	PageResponseDto<UserResponseDto> getPage(Pageable pageable);
	
	Set<UserResponseDto> getAdministration();
	
	@Transactional(readOnly = false)
    int create(UserCreateDto userDto);
	
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	@Transactional(readOnly = false)
	void updateRole(int id, UserRoleUpdateDto userDto);
	
	@Transactional(readOnly = false)
	void updateSecurityData(int id, UserSecurityDataUpdateDto userDto);
	
	@PreAuthorize("hasRole('HEAD_ADMINISTRATOR')")
	@Transactional(readOnly = false)
	void delete(int id);
	
	@Transactional(readOnly = false)
	void createSecurityTokenForUser(String email);
}
