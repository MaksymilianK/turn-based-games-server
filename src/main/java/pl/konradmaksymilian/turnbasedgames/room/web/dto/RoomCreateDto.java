package pl.konradmaksymilian.turnbasedgames.room.web.dto;

import java.util.Collections;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsRequestDto;
import pl.konradmaksymilian.turnbasedgames.room.data.ChatPolicy;
import pl.konradmaksymilian.turnbasedgames.room.data.RoomPrivacy;

public class RoomCreateDto {
	
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
	private final GameSettingsRequestDto gameSettings;

	public RoomCreateDto(@JsonProperty("areObserversAllowed") Boolean areObserversAllowed, 
			@JsonProperty("privacy") RoomPrivacy privacy, @JsonProperty("chatPolicy") ChatPolicy chatPolicy,
			@JsonProperty("invitations") Set<Integer> invitedUsers, 
			@JsonProperty("gameSettings") GameSettingsRequestDto gameSettings) {
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

	public GameSettingsRequestDto getGameSettings() {
		return gameSettings;
	}
}
