package pl.konradmaksymilian.turnbasedgames.player.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import pl.konradmaksymilian.turnbasedgames.player.validator.Nick;

public class PlayerCreateDto {

	@NotNull
	@Nick
	private String nick;
	
	@NotNull
	@Email
	private String email;
	
	@NotNull
	@Size(min = 5, max = 75)
	private String password;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
