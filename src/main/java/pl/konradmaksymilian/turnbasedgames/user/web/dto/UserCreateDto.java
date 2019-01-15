package pl.konradmaksymilian.turnbasedgames.user.web.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.core.validator.DoesNotStartWithIgnoreCase;
import pl.konradmaksymilian.turnbasedgames.user.business.validator.Nick;

public class UserCreateDto {

	@NotNull
	@Nick
	@DoesNotStartWithIgnoreCase("guest")
	private final String nick;
	
	@NotNull
	@Email
	private final String email;
	
	@NotNull
	@Size(min = 5, max = 75)
	private final String password;
	
	public UserCreateDto(@JsonProperty("nick") String nick, @JsonProperty("email") String email, 
			@JsonProperty("password") String password) {
		this.nick = nick;
		this.email = email;
		this.password = password;
	}

	public String getNick() {
		return nick;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

}
