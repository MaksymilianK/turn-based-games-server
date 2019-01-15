package pl.konradmaksymilian.turnbasedgames.room.business;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomResponse;

public class RoomMessageSender {
	
	public static final String DESTINATION_PREFIX = "/topic/game-rooms";
	private final int roomId; 
	private final SimpMessagingTemplate messagingTemplate;

	public RoomMessageSender(int roomId, SimpMessagingTemplate messagingTemplate) {
		this.roomId = roomId;
		this.messagingTemplate = messagingTemplate;
	}
	
	public void publish(RoomResponse roomResponse) {
		messagingTemplate.convertAndSend(buildDestination(roomId), roomResponse);
	}
	
	public void sendToUsers(RoomResponse response, String... users) {
		for (String user : users) {
			messagingTemplate.convertAndSendToUser(user, buildDestination(roomId), response);
		}
	}
	
	private String buildDestination(int roomId) {
		return DESTINATION_PREFIX + "/" + roomId;
	}
}
