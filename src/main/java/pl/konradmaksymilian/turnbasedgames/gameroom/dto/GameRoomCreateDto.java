package pl.konradmaksymilian.turnbasedgames.gameroom.dto;

import java.util.Collections;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.GameSettingsDto;
import pl.konradmaksymilian.turnbasedgames.gameroom.ChatPolicy;
import pl.konradmaksymilian.turnbasedgames.gameroom.RoomPrivacy;

public class GameRoomCreateDto {
	
	@NotNull
	private final Boolean areObserversAllowed;
	
	@NotNull
	private final RoomPrivacy privacy;
	
	@NotNull
	private final ChatPolicy chatPolicy;
	
	@Size(max = 5)
	private final Set<Integer> invitedUsers;
	
	@NotNull
	@Valid
	private final GameSettingsDto gameSettings;

	public GameRoomCreateDto(@JsonProperty("areObserversAllowed") Boolean areObserversAllowed, 
			@JsonProperty("privacy") RoomPrivacy privacy, @JsonProperty("chatPolicy") ChatPolicy chatPolicy,
			@JsonProperty("invitations") Set<Integer> invitedUsers, 
			@JsonProperty("gameSettings") GameSettingsDto gameSettings) {
		this.areObserversAllowed = areObserversAllowed;
		this.privacy = privacy;
		this.chatPolicy = chatPolicy;
		this.invitedUsers = (invitedUsers == null) ? Collections.emptySet() : invitedUsers;
		this.gameSettings = gameSettings;
	}

	public Boolean areObserversAllowed() {
		return areObserversAllowed;
	}

	public RoomPrivacy getPrivacy() {
		return privacy;
	}

	public ChatPolicy getChatPolicy() {
		return chatPolicy;
	}
	
	public Set<Integer> getInvitedUsers() {
		return invitedUsers;
	}

	public GameSettingsDto getGameSettings() {
		return gameSettings;
	}
}
