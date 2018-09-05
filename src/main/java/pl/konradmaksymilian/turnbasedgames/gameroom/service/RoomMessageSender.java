package pl.konradmaksymilian.turnbasedgames.gameroom.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.game.core.action.event.GameEvent;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.PublishableRoomMessage;

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
	
	public void sendGameEvent(int roomId, GameEvent event) {
		messagingTemplate.convertAndSend(getDestination(roomId), event);
	}
	
	private String getDestination(int roomId) {
		return DESTINATION_PREFIX + "/" + roomId;
	}
}
