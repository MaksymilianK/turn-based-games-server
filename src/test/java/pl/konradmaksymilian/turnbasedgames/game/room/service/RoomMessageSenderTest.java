package pl.konradmaksymilian.turnbasedgames.game.room.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.event.GameEventDto;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.PublishableUserEscapeMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.service.RoomMessageSender;

public class RoomMessageSenderTest {

	private RoomMessageSender messageSender;
	
	@Mock
	private SimpMessagingTemplate messagingTemplate;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		messageSender = new RoomMessageSender(messagingTemplate);
	}
	
	@Test
	public void sendRoomMessage_sendsMessage() {
		var message = new PublishableUserEscapeMessage(15);
		messageSender.sendRoomMessage(5, message);
		
		verify(messagingTemplate, times(1)).convertAndSend("/topic/game-rooms/5", message);
	}
	
	@Test
	public void sendGameCommand_sendsCoomand() {
		var event = Mockito.mock(GameEventDto.class);
		messageSender.sendGameEvent(5, event);
		
		verify(messagingTemplate, times(1)).convertAndSend("/topic/game-rooms/5", event	);
	}
}
