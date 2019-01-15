package pl.konradmaksymilian.turnbasedgames.user.web.controller;

import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import pl.konradmaksymilian.turnbasedgames.core.dto.PageResponseDto;
import pl.konradmaksymilian.turnbasedgames.core.dto.TextResponseDto;
import pl.konradmaksymilian.turnbasedgames.user.business.service.UserService;
import pl.konradmaksymilian.turnbasedgames.user.business.validator.Nick;
import pl.konradmaksymilian.turnbasedgames.user.business.validator.UserPageable;
import pl.konradmaksymilian.turnbasedgames.user.web.dto.UserCreateDto;
import pl.konradmaksymilian.turnbasedgames.user.web.dto.UserResponseDto;
import pl.konradmaksymilian.turnbasedgames.user.web.dto.UserRoleUpdateDto;
import pl.konradmaksymilian.turnbasedgames.user.web.dto.UserSecurityDataUpdateDto;
import pl.konradmaksymilian.turnbasedgames.user.web.dto.UsersStatsResponseDto;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 5000)
@RestController
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@CrossOrigin(origins = "http://localhost:4200", maxAge = 5000)
	@GetMapping("/stats")
	public UsersStatsResponseDto getStats() {
		return userService.getStats();
	}
	
	@GetMapping("")
	public PageResponseDto<UserResponseDto> getPage(@UserPageable Pageable pageable) {
		return userService.getPage(pageable);
	}
	
	@GetMapping("/administration")
	public Set<UserResponseDto> getAdministration() {
		return userService.getAdministration();
	}
	
	@GetMapping("/current")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public UserResponseDto getCurrent() {
		return userService.getCurrent();
	}
	
	@GetMapping("/{id}")
	public UserResponseDto get(@PathVariable int id) {
		return userService.get(id);
	}
	
	@GetMapping(path = "", params = "nick")
	public UserResponseDto getByNick(@RequestParam @Nick String nick) {
		return userService.getByNick(nick);
	}
	
	@CrossOrigin(origins = "http://localhost:4200", maxAge = 5000)
	@PostMapping("")
	public ResponseEntity<TextResponseDto> create(@RequestBody @Validated UserCreateDto userDto) {
		int id = userService.create(userDto);
		var location = ServletUriComponentsBuilder.fromCurrentRequest().pathSegment(Integer.toString(id)).build().toUri();
		
		return ResponseEntity.created(location)
				.body(new TextResponseDto("Created a new user!"));
	}
	
	@PutMapping("/{id}/role")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateRole(@PathVariable int id, @RequestBody @Validated UserRoleUpdateDto userDto) {
		userService.updateRole(id, userDto);
	}
	
	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateSecurityData(@PathVariable int id, @RequestBody @Validated UserSecurityDataUpdateDto userDto) {
		userService.updateSecurityData(id, userDto);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable int id) {
		userService.delete(id);
	}
}
