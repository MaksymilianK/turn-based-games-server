package pl.konradmaksymilian.turnbasedgames.room.web.dto;

import java.util.List;
import java.util.Set;

import pl.konradmaksymilian.turnbasedgames.user.web.dto.UserResponseDto;

public class RoomStatusDto {
	
	private final RoomSettingsResponseDto gameRoomSettings;
	private final List<UserResponseDto> users;
	private final Set<InvitationResponseDto> invitations;
	
	public RoomStatusDto(RoomSettingsResponseDto gameRoomSettings, List<UserResponseDto> users,
			Set<InvitationResponseDto> invitations) {
		this.gameRoomSettings = gameRoomSettings;
		this.users = users;
		this.invitations = invitations;
	}
	
	public RoomSettingsResponseDto getRoomSettings() {
		return gameRoomSettings;
	}

	public List<UserResponseDto> getUsers() {
		return users;
	}
	
	public Set<InvitationResponseDto> getInvitations() {
		return invitations;
	}
}
