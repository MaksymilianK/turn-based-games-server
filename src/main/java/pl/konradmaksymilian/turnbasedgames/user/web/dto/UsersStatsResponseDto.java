package pl.konradmaksymilian.turnbasedgames.user.web.dto;

public class UsersStatsResponseDto {

	private final int registeredUsers;
	private final int currentlyLoggedUsers;
	
	public UsersStatsResponseDto(int registeredUsers, int currentlyLoggedUsers) {
		this.registeredUsers = registeredUsers;
		this.currentlyLoggedUsers = currentlyLoggedUsers;
	}
	
	public int getRegisteredUsers() {
		return registeredUsers;
	}

	public int getCurrentlyLoggedUsers() {
		return currentlyLoggedUsers;
	}
}
