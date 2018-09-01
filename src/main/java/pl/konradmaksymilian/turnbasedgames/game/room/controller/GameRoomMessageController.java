package pl.konradmaksymilian.turnbasedgames.game.room.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.command.GameCommand;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.GameRoomDetailsResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.PublishableRoomMessage;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.RoomMessage;
import pl.konradmaksymilian.turnbasedgames.game.room.service.GameRoomService;

@MessageMapping("/game-rooms/{id}")
public class GameRoomMessageController {
	
	private GameRoomService roomService;

	public GameRoomMessageController(GameRoomService roomService) {
		this.roomService = roomService;
	}
	
	public PublishableRoomMessage deliverMessage(@DestinationVariable int roomId, RoomMessage message) {
		return roomService.processMessage(roomId, message);
	}
	
	@SubscribeMapping("")
	public GameRoomDetailsResponseDto joinRoom(@DestinationVariable int roomId) {
		return roomService.joinRoom(roomId);
	}
	
	@MessageMapping("/game")
	public void deliverCommand(@DestinationVariable int roomId, GameCommand command) {
		roomService.deliverCommand(roomId, command);
	}
}
