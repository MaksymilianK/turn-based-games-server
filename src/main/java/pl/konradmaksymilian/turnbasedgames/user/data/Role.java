package pl.konradmaksymilian.turnbasedgames.user.data;

public enum Role {

	GUEST,
	PLAYER,
	HELPER,
	MODERATOR,
	ADMINISTRATOR,
	HEAD_ADMINISTRATOR;
	
	public boolean isHigherThan(Role role) {
		return compareTo(role) > 0;
	}
	
	public boolean isHigherThanOrEqual(Role role) {
		return compareTo(role) >= 0;
	}
	
	public boolean isLowerThan(Role role) {
		return compareTo(role) < 0;
	}
	
	public boolean isLowerThanOrEqual(Role role) {
		return compareTo(role) <= 0;
	}
	
	public String toString() {
		return "ROLE_" + name();
	}
}
