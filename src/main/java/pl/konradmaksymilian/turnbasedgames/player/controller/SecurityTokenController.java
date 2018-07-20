package pl.konradmaksymilian.turnbasedgames.player.controller;

import javax.validation.constraints.Email;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pl.konradmaksymilian.turnbasedgames.player.service.PlayerService;

@RestController
@RequestMapping(path = "/security-tokens")
@Validated
public class SecurityTokenController {

	private PlayerService playerService;
	
	public SecurityTokenController(PlayerService playerService) {
		this.playerService = playerService;
	}

	@PostMapping(path = "", params = "email")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void create(@RequestParam @Email String email) {
		playerService.createSecurityTokenForPlayer(email);
	}
}
