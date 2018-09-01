package pl.konradmaksymilian.turnbasedgames.game.room.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import pl.konradmaksymilian.turnbasedgames.core.dto.PageResponseDto;
import pl.konradmaksymilian.turnbasedgames.core.dto.TextResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.GameRoomCreateDto;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.GameRoomResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.room.service.GameRoomService;

@RestController
@RequestMapping(path = "/game-rooms", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GameRoomController {

	private GameRoomService roomService;
	
	public GameRoomController(GameRoomService roomService) {
		this.roomService = roomService;
	}
	
	@GetMapping("")
	public PageResponseDto<GameRoomResponseDto> getPage(Pageable pageable, @RequestParam Game game) {
		return roomService.getPage(pageable, game);
	}
	
	@PostMapping("")
	public ResponseEntity<TextResponseDto> create(@RequestBody GameRoomCreateDto roomDto) {
		int id = roomService.create(roomDto);
		var location = ServletUriComponentsBuilder.fromCurrentRequest().pathSegment(Integer.toString(id)).build().toUri();
		
		return ResponseEntity.created(location).body(new TextResponseDto("Created a new game room"));
	}
}
