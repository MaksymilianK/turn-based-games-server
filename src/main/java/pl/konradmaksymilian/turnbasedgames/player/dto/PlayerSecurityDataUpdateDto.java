package pl.konradmaksymilian.turnbasedgames.player.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import pl.konradmaksymilian.turnbasedgames.player.validator.Nick;
import pl.konradmaksymilian.turnbasedgames.player.validator.NotEmptyPlayerSecurityDataUpdateDto;
import pl.konradmaksymilian.turnbasedgames.player.validator.SecurityToken;

@NotEmptyPlayerSecurityDataUpdateDto
public class PlayerSecurityDataUpdateDto {
	
	@Nick
	private String newNick;
	
	@Email
	private String newEmail;
	
	@Size(min = 5, max = 75)
	private String newPassword;
	
	@NotNull
	@SecurityToken
	private String securityToken;

	public String getNewNick() {
		return newNick;
	}

	public void setNewNick(String newNick) {
		this.newNick = newNick;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}
}
