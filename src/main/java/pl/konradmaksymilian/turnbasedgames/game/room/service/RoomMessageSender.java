package pl.konradmaksymilian.turnbasedgames.game.room.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.command.PublishableGameCommand;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.PublishableRoomMessage;

@Component
public class RoomMessageSender {
	
	public static final String DESTINATION_PREFIX = "/topic/game-rooms";

	private SimpMessagingTemplate messagingTemplate;
	
	public RoomMessageSender(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	
	public void sendRoomMessage(int roomId, PublishableRoomMessage message) {
		messagingTemplate.convertAndSend(getDestination(roomId), message);
	}
	
	public void sendGameCommand(int roomId, PublishableGameCommand command) {
		messagingTemplate.convertAndSend(getDestination(roomId), command);
	}
	
	private String getDestination(int roomId) {
		return DESTINATION_PREFIX + "/" + roomId;
	}
}
