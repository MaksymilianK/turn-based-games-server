package pl.konradmaksymilian.turnbasedgames.game.room.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.command.PublishableGameCommand;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.PublishableUserEscapeMessage;

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
		var command = Mockito.mock(PublishableGameCommand.class);
		messageSender.sendGameCommand(5, command);
		
		verify(messagingTemplate, times(1)).convertAndSend("/topic/game-rooms/5", command);
	}
}
