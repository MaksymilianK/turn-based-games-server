package pl.konradmaksymilian.turnbasedgames.player.dto;

import pl.konradmaksymilian.turnbasedgames.player.Role;

public class PlayerResponseDto {
	
	private int id;
	
	private String nick;
	
	private Role role;

	protected PlayerResponseDto() {}
	
	public PlayerResponseDto(int id, String nick, Role role) {
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
