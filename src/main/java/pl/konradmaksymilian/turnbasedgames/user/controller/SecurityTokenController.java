package pl.konradmaksymilian.turnbasedgames.user.controller;

import javax.validation.constraints.Email;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pl.konradmaksymilian.turnbasedgames.user.service.UserService;

@RestController
@RequestMapping("/security-tokens")
@Validated
public class SecurityTokenController {

	private UserService userService;
	
	public SecurityTokenController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping(path = "", params = "email")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void create(@RequestParam @Email String email) {
		userService.createSecurityTokenForUser(email);
	}
}
