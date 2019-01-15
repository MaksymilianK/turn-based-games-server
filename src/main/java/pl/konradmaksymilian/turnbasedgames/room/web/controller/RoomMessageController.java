package pl.konradmaksymilian.turnbasedgames.room.web.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;

import pl.konradmaksymilian.turnbasedgames.room.business.service.RoomService;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request.RoomRequest;

@MessageMapping("/game-rooms/{id}")
public class RoomMessageController {
	
	private RoomService roomService;

	public RoomMessageController(RoomService roomService) {
		this.roomService = roomService;
	}
	
	@MessageMapping("")
	public void deliverMessage(@DestinationVariable int roomId, RoomRequest message) {
		roomService.processMessage(roomId, message);
	}
	
	@SubscribeMapping("")
	public void joinRoom(@DestinationVariable int roomId) {
		roomService.joinRoom(roomId);
	}
}
