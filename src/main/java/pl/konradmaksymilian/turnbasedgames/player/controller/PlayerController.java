package pl.konradmaksymilian.turnbasedgames.player.controller;

import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerCreateDto;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerResponseDto;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerRoleUpdateDto;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerSecurityDataUpdateDto;
import pl.konradmaksymilian.turnbasedgames.player.service.PlayerService;
import pl.konradmaksymilian.turnbasedgames.player.validator.Nick;
import pl.konradmaksymilian.turnbasedgames.player.validator.PlayerPageable;

@RestController
@RequestMapping(path = "/players", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PlayerController {
	
	private PlayerService playerService;
	
	public PlayerController(PlayerService playerService) {
		this.playerService = playerService;
	}

	@GetMapping("")
	public PageResponseDto<PlayerResponseDto> getPage(@PlayerPageable Pageable pageable) {
		return playerService.getPage(pageable);
	}
	
	@GetMapping("/administration")
	public Set<PlayerResponseDto> getAdministration() {
		return playerService.getAdministration();
	}
	
	@GetMapping("/current")
	public PlayerResponseDto getCurrent() {
		return playerService.getCurrent();
	}
	
	@GetMapping("/{id}")
	public PlayerResponseDto get(@PathVariable int id) {
		return playerService.get(id);
	}
	
	@GetMapping(path = "", params = "nick")
	public PlayerResponseDto get(@RequestParam @Nick String nick) {
		return playerService.getByNick(nick);
	}
	
	@PostMapping("")
	public ResponseEntity<TextResponseDto> create(@RequestBody @Validated PlayerCreateDto playerDto) {
		int id = playerService.create(playerDto);
		var location = ServletUriComponentsBuilder.fromCurrentRequest().pathSegment(Integer.toString(id)).build().toUri();
		
		return ResponseEntity.created(location)
				.body(new TextResponseDto("Created a new player!"));
	}
	
	@PutMapping("/{id}/role")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateRole(@PathVariable int id, @RequestBody @Validated PlayerRoleUpdateDto playerDto) {
		playerService.updateRole(id, playerDto);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateSecurityData(@PathVariable int id, @RequestBody @Validated PlayerSecurityDataUpdateDto playerDto) {
		playerService.updateSecurityData(id, playerDto);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable int id) {
		playerService.delete(id);
	}
}
