package pl.konradmaksymilian.turnbasedgames.room.business;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RoomMessageSenderFactory {

	private SimpMessagingTemplate messagingTemplate;
	
	public RoomMessageSenderFactory(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	public RoomMessageSender create(int roomId) {
		return new RoomMessageSender(roomId, messagingTemplate);
	}
}
