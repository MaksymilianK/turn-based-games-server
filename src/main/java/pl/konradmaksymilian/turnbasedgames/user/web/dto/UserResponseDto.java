package pl.konradmaksymilian.turnbasedgames.user.web.dto;

import pl.konradmaksymilian.turnbasedgames.user.data.Role;

public class UserResponseDto {
	
	private int id;
	
	private String nick;
	
	private Role role;

	protected UserResponseDto() {}
	
	public UserResponseDto(int id, String nick, Role role) {
		this.id = id;
		this.nick = nick;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
