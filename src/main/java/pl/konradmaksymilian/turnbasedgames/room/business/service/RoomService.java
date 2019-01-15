package pl.konradmaksymilian.turnbasedgames.room.business.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import pl.konradmaksymilian.turnbasedgames.core.dto.PageResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.core.data.Game;
import pl.konradmaksymilian.turnbasedgames.room.data.entity.Room;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomCreateDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomResponseDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomsStatsResponseDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request.RoomRequest;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomDetailsResponse;

public interface RoomService {
		
	Optional<Room> find(int id);
	
	@PreAuthorize("isAuthenticated()")
	PageResponseDto<RoomResponseDto> getPage(Pageable pageable, Game game);

	@PreAuthorize("hasRole('PLAYER')")
	int create(RoomCreateDto roomDto);
	
	@PreAuthorize("isAuthenticated()")
	RoomDetailsResponse joinRoom(int roomId);
	
	void removeUser(int userId);
		
	@PreAuthorize("isAuthenticated()")
	void processMessage(int roomId, RoomRequest message);

	RoomsStatsResponseDto getStats();
}
