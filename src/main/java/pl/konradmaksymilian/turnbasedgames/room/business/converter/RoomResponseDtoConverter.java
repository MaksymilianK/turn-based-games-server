package pl.konradmaksymilian.turnbasedgames.room.business.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.core.converter.ModelToDtoConverter;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.GameStatus;
import pl.konradmaksymilian.turnbasedgames.room.data.entity.Room;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.InvitationResponseDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomResponseDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomSettingsResponseDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomStatusDto;
import pl.konradmaksymilian.turnbasedgames.user.business.converter.UserResponseDtoConverter;
import pl.konradmaksymilian.turnbasedgames.user.data.entity.User;

@Component
public class RoomResponseDtoConverter implements ModelToDtoConverter<Room, RoomResponseDto> {
	
	private UserResponseDtoConverter userConverter;
	
	public RoomResponseDtoConverter(UserResponseDtoConverter userConverter) {
		this.userConverter = userConverter;
	}

	@Override
	public RoomResponseDto convert(Room room) {
		return RoomResponseDto.builder()
				.id(room.getId())
				.creationTime(room.getCreationTime().toEpochMilli())
				.users(getUsersNicks(room.getUsers()))
				.players(room.getGameEngine().getPlayers().size())
				.roomSettings(extractSettings(room))
				.gameSettings(room.getGameEngine().getSettings())
				.isNotStarted(room.getGameEngine().getStatus().equals(GameStatus.NOT_STARTED))
				.build();
	}
	
	public RoomStatusDto convertStatus(Room room) {
		var users = room.getUsers().stream().map(user -> userConverter.convert(user)).collect(Collectors.toList()); 
		var invitations = room.getInvitations().stream().map(invitation -> new InvitationResponseDto(
				invitation.getSenderId(), invitation.getInviteeId())).collect(Collectors.toSet());
		return new RoomStatusDto(extractSettings(room), users, invitations);
	}
	
	public RoomSettingsResponseDto extractSettings(Room room) {
		return new RoomSettingsResponseDto(room.getSettings().areObserversAllowed(),
				room.getSettings().getPrivacy(), room.getSettings().getChatPolicy());
	}
	
	private List<String> getUsersNicks(List<User> users) {
		return users.stream()
				.map(user -> user.getNick())
				.collect(Collectors.toList());
	}
}
