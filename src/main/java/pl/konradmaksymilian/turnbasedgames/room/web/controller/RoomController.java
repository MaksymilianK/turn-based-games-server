package pl.konradmaksymilian.turnbasedgames.room.web.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import pl.konradmaksymilian.turnbasedgames.core.dto.PageResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.core.data.Game;
import pl.konradmaksymilian.turnbasedgames.room.business.service.RoomService;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomCreateDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomCreationResponseDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomResponseDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomsStatsResponseDto;

@RestController
@RequestMapping(path = "/game-rooms", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RoomController {

	private RoomService roomService;
	
	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}
	
	@CrossOrigin(origins = "http://localhost:4200", maxAge = 5000)
	@GetMapping("/stats")
	public RoomsStatsResponseDto getStats() {
		return roomService.getStats();
	}
	
	@GetMapping("")
	public PageResponseDto<RoomResponseDto> getPage(@RequestParam Game game, Pageable pageable) {
		return roomService.getPage(pageable, game);
	}
	
	@PostMapping("")
	public ResponseEntity<RoomCreationResponseDto> create(@RequestBody RoomCreateDto roomDto) {
		int id = roomService.create(roomDto);
		var location = ServletUriComponentsBuilder.fromCurrentRequest().pathSegment(Integer.toString(id)).build().toUri();
		
		return ResponseEntity.created(location).body(new RoomCreationResponseDto(id));
	}
}
