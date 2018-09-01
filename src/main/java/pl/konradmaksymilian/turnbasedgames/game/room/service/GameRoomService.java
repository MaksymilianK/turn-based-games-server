package pl.konradmaksymilian.turnbasedgames.game.room.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import pl.konradmaksymilian.turnbasedgames.core.dto.PageResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.command.GameCommand;
import pl.konradmaksymilian.turnbasedgames.game.room.GameRoom;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.GameRoomCreateDto;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.GameRoomDetailsResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.GameRoomResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.PublishableRoomMessage;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.RoomMessage;

public interface GameRoomService {
		
	Optional<GameRoom> find(int id);
	
	@PreAuthorize("isAuthenticated()")
	PageResponseDto<GameRoomResponseDto> getPage(Pageable pageable, Game game);

	@PreAuthorize("hasRole('PLAYER')")
	int create(GameRoomCreateDto roomDto);
	
	@PreAuthorize("isAuthenticated()")
	GameRoomDetailsResponseDto joinRoom(int roomId);
	
	void removeUser(int userId);
		
	@PreAuthorize("isAuthenticated()")
	PublishableRoomMessage processMessage(int roomId, RoomMessage message);
	
	@PreAuthorize("isAuthenticated()")
	void deliverCommand(int roomId, GameCommand command);
}
