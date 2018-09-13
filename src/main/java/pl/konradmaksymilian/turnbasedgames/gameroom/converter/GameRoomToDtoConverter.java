package pl.konradmaksymilian.turnbasedgames.gameroom.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.core.converter.EntityToDtoConverter;
import pl.konradmaksymilian.turnbasedgames.game.dto.GameResponseDtoFactory;
import pl.konradmaksymilian.turnbasedgames.gameroom.GameRoom;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.GameRoomDetailsResponseDto;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.GameRoomResponseDto;
import pl.konradmaksymilian.turnbasedgames.user.entity.User;

@Component
public class GameRoomToDtoConverter implements EntityToDtoConverter<GameRoom, GameRoomResponseDto> {

	private GameResponseDtoFactory gameDtoFactory;
	
	public GameRoomToDtoConverter(GameResponseDtoFactory gameDtoFactory) {
		this.gameDtoFactory = gameDtoFactory;
	}
	
	@Override
	public GameRoomResponseDto convert(GameRoom entity) {
		return new GameRoomResponseDto.Builder()
				.id(entity.getId())
				.creationTime(entity.getCreationTime().getEpochSecond())
				.areObserversAllowed(entity.areObserversAllowed())
				.chatPolicy(entity.getChatPolicy())
				.privacy(entity.getPrivacy())
				.users(getUsersNicks(entity.getUsers()))
				.game(entity.getGameEngine().getGame())
				.players(entity.getGameEngine().countPlayers())
				.maxPlayers(entity.getGameEngine().getMaxPlayers())
				.build();
	}
	
	public GameRoomDetailsResponseDto convertDetailed(GameRoom room) {
		return new GameRoomDetailsResponseDto.Builder()
				.id(room.getId())
				.areObserversAllowed(room.areObserversAllowed())
				.chatPolicy(room.getChatPolicy())
				.creationTime(room.getCreationTime().toEpochMilli())
				.privacy(room.getPrivacy())
				.users(getUsersNicks(room.getUsers()))
				.gameDto(gameDtoFactory.create(room.getGameEngine()))
				.build();
	}
	
	public List<String> getUsersNicks(List<User> users) {
		return users.stream()
				.map(user -> user.getNick())
				.collect(Collectors.toList());
	}
}
