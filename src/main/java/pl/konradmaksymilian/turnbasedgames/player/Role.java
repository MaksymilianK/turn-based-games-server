package pl.konradmaksymilian.turnbasedgames.player;

public enum Role {

	GUEST,
	PLAYER,
	HELPER,
	MODERATOR,
	ADMINISTRATOR,
	HEAD_ADMINISTRATOR;
	
	public String toString() {
		return "ROLE_" + name();
	}
}
