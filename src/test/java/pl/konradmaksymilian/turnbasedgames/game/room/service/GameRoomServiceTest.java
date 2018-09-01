package pl.konradmaksymilian.turnbasedgames.game.room.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;

import pl.konradmaksymilian.turnbasedgames.core.dto.PageResponseDto;
import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;
import pl.konradmaksymilian.turnbasedgames.core.exception.ResourceNotFoundException;
import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.GameEngineTestUtils;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameStatus;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.dto.DontGetAngryGameSettingsDto;
import pl.konradmaksymilian.turnbasedgames.game.room.ChatPolicy;
import pl.konradmaksymilian.turnbasedgames.game.room.GameRoom;
import pl.konradmaksymilian.turnbasedgames.game.room.Invitation;
import pl.konradmaksymilian.turnbasedgames.game.room.RoomPrivacy;
import pl.konradmaksymilian.turnbasedgames.game.room.RoomTestUtils;
import pl.konradmaksymilian.turnbasedgames.game.room.converter.GameRoomToDtoConverter;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.GameRoomCreateDto;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.GameRoomDetailsResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.GameRoomResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.ChatMessage;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.GameChangeMessage;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.HostChangeMessage;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.InvitationMessage;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.PublishableChatMessage;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.PublishableGameChangeMessage;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.PublishableInvitationMessage;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.PublishableRoomSettingsChangeMessage;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.PublishableUserEscapeMessage;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.message.RoomSettingsChangeMessage;
import pl.konradmaksymilian.turnbasedgames.game.room.repository.GameRoomManager;
import pl.konradmaksymilian.turnbasedgames.user.Role;
import pl.konradmaksymilian.turnbasedgames.user.UserTestUtils;
import pl.konradmaksymilian.turnbasedgames.user.service.UserService;

public class GameRoomServiceTest {

	private GameRoomService roomService;
	
	@Mock
	private GameRoomManager roomManager;
	
	@Mock
	private UserService userService;
	
	@Mock
	private GameRoomToDtoConverter roomConverter;
	
	@Mock
	private RoomMessageSender messageSender;
	
	@Mock
	private GameEngineFactory engineFactory;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		roomService = new GameRoomServiceImpl(roomManager, userService, roomConverter, messageSender, engineFactory);
	}
	
	@Test
	public void getPage_returnsPageDto() {
		var mockPageable = PageRequest.of(1, 5);
		var mockCurrent = UserTestUtils.mockUser(7);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoomPage = new PageImpl<GameRoom>(Collections.emptyList(), mockPageable, 20);
		when(roomManager.getPage(mockPageable, mockCurrent, Game.DONT_GET_ANGRY)).thenReturn(mockRoomPage);
		var mockRoomPageResponseDto = new PageResponseDto<GameRoomResponseDto>(null, null, 0, 0, 0); 
		when(roomConverter.convertPage(mockRoomPage)).thenReturn(mockRoomPageResponseDto);
		
		assertThat(roomService.getPage(mockPageable, Game.DONT_GET_ANGRY)).isEqualTo(mockRoomPageResponseDto);
	}
	
	@Test
	public void create_savesNewRoom() {
		var mockCurrent = UserTestUtils.mockUser(6);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var invitedUsers = new HashSet<Integer>();
		invitedUsers.add(1);
		invitedUsers.add(8);
		var gameSettings = new DontGetAngryGameSettingsDto(40, 4, 1500);
		var dto = new GameRoomCreateDto(false, RoomPrivacy.GUEST_FREE, ChatPolicy.CHAT_OFF, invitedUsers, gameSettings);
		var mockEngine = GameEngineTestUtils.mockEngine();
		when(engineFactory.create(gameSettings)).thenReturn(mockEngine);
		var mockRoom = RoomTestUtils.mockRoom();
		when(roomManager.save(any(GameRoom.class))).thenReturn(mockRoom);
		when(roomManager.countByUserId(6)).thenReturn(2);
		
		roomService.create(dto);
		 
		verify(roomManager, times(1)).save(argThat((GameRoom room) -> room.getChatPolicy().equals(ChatPolicy.CHAT_OFF)
				&& room.areObserversAllowed() == false && room.getGameEngine().equals(mockEngine) 
				&& room.getHost().equals(mockCurrent) && room.getPrivacy().equals(RoomPrivacy.GUEST_FREE)
				&& room.containsUser(mockCurrent.getId()) && room.countUsers() == 1 && room.isUserInvited(1)
				&& room.isUserInvited(8)));
	}
	
	@Test
	public void create_returnsRoomId() {
		when(userService.findCurrent()).thenReturn(Optional.of(UserTestUtils.mockUser(6)));
		var gameSettings = new DontGetAngryGameSettingsDto(40, 4, 1500);
		var dto = new GameRoomCreateDto(false, RoomPrivacy.GUEST_FREE, ChatPolicy.CHAT_OFF, null, gameSettings);
		var mockEngine = GameEngineTestUtils.mockEngine();
		when(engineFactory.create(gameSettings)).thenReturn(mockEngine);
		var mockRoom = RoomTestUtils.mockRoom(5);
		when(roomManager.save(any(GameRoom.class))).thenReturn(mockRoom);
		when(roomManager.countByUserId(6)).thenReturn(1);
		
		assertThat(roomService.create(dto)).isEqualTo(5);
	}
	
	@Test
	public void create_givenCurrentInMoreThan2Rooms_returnsRoomId() {
		when(userService.findCurrent()).thenReturn(Optional.of(UserTestUtils.mockUser(6)));
		var gameSettings = new DontGetAngryGameSettingsDto(40, 4, 1500);
		var dto = new GameRoomCreateDto(false, RoomPrivacy.GUEST_FREE, ChatPolicy.CHAT_OFF, null, gameSettings);
		var mockEngine = GameEngineTestUtils.mockEngine();
		when(engineFactory.create(gameSettings)).thenReturn(mockEngine);
		var mockRoom = RoomTestUtils.mockRoom(5);
		when(roomManager.save(any(GameRoom.class))).thenReturn(mockRoom);
		when(roomManager.countByUserId(6)).thenReturn(3);
		
		assertThatExceptionOfType(BadOperationException.class)
				.isThrownBy(() -> roomService.create(dto))
				.withMessage("Cannot join to more than 3 rooms at the same time");
	}
	
	@Test
	public void removeUser_removesUserFromRooms() {
		var mockUser = UserTestUtils.mockUser(4);
		var rooms = new HashSet<GameRoom>();
		rooms.add((new RoomTestUtils.MockRoomBuilder())
				.user(mockUser)
				.build());
		rooms.add((new RoomTestUtils.MockRoomBuilder())
				.user(UserTestUtils.mockGuest())
				.user(mockUser)
				.build());
		when(roomManager.findByUserId(4)).thenReturn(rooms);
		
		roomService.removeUser(4);
		
		assertThat(rooms).noneMatch(room -> room.containsUser(4));
	}
	
	@Test
	public void removeUser_givenLastUserInRooms_deletesRooms() {
		var mockUser = UserTestUtils.mockUser(4);
		var rooms = new HashSet<GameRoom>();
		rooms.add((new RoomTestUtils.MockRoomBuilder())
				.id(0)
				.host(mockUser) //last user in the room
				.build());
		rooms.add((new RoomTestUtils.MockRoomBuilder())
				.id(57)
				.user(mockUser)
				.build());
		rooms.add((new RoomTestUtils.MockRoomBuilder())
				.id(27)
				.host(mockUser)
				.user(UserTestUtils.mockUser(5))
				.build());
		rooms.add((new RoomTestUtils.MockRoomBuilder())
				.id(257)
				.host(mockUser) //last user in the room
				.build());
		rooms.add((new RoomTestUtils.MockRoomBuilder())
				.id(25)
				.host(mockUser)
				.user(UserTestUtils.mockGuest())
				.build());
		when(roomManager.findByUserId(4)).thenReturn(rooms);
		when(roomManager.delete(0)).thenReturn(true);
		when(roomManager.delete(57)).thenReturn(true);
		when(roomManager.delete(27)).thenReturn(true);
		when(roomManager.delete(257)).thenReturn(true);
		when(roomManager.delete(25)).thenReturn(true);
		
		roomService.removeUser(4);
		
		verify(roomManager, times(1)).delete(0);
		verify(roomManager, times(1)).delete(257);
		verify(roomManager, never()).delete(57);
		verify(roomManager, never()).delete(27);
		verify(roomManager, never()).delete(25);
	}
	
	@Test
	public void removeUser_givenHost_changesHost() {
		var mockUser = UserTestUtils.mockUser(4);
		var rooms = Arrays.asList(
				(new RoomTestUtils.MockRoomBuilder())
						.id(0)
						.host(mockUser) //last user in the room
						.build(),
				(new RoomTestUtils.MockRoomBuilder())
						.id(57)
						.user(mockUser)
						.build(),
				(new RoomTestUtils.MockRoomBuilder())
						.id(27)
						.host(mockUser)
						.user(UserTestUtils.mockUser(5))
						.build(),
				(new RoomTestUtils.MockRoomBuilder())
						.id(257)
						.host(mockUser) //last user in the room
						.build(),
				(new RoomTestUtils.MockRoomBuilder())
						.id(25)
						.host(mockUser)
						.user(UserTestUtils.mockGuest(-5))
						.build()
				);
		when(roomManager.findByUserId(4)).thenReturn(Sets.newHashSet(rooms));
		when(roomManager.delete(0)).thenReturn(true);
		when(roomManager.delete(57)).thenReturn(true);
		when(roomManager.delete(27)).thenReturn(true);
		when(roomManager.delete(257)).thenReturn(true);
		when(roomManager.delete(25)).thenReturn(true);
		
		roomService.removeUser(4);
		
		assertThat(rooms.get(1).getHost().getId()).isEqualTo(126);
		assertThat(rooms.get(2).getHost().getId()).isEqualTo(5);
		assertThat(rooms.get(4).getHost().getId()).isEqualTo(-5);
	}
	
	@Test
	public void removeUser_sendsMessages() {
		var mockUser = UserTestUtils.mockUser(4);
		var rooms = new HashSet<GameRoom>();
		rooms.add((new RoomTestUtils.MockRoomBuilder())
				.id(0)
				.user(mockUser)
				.build());
		rooms.add((new RoomTestUtils.MockRoomBuilder())
				.id(57)
				.user(UserTestUtils.mockGuest())
				.user(mockUser)
				.build());
		rooms.add((new RoomTestUtils.MockRoomBuilder())
				.id(27)
				.host(mockUser) //last user in the room
				.build());
		when(roomManager.findByUserId(4)).thenReturn(rooms);
		when(roomManager.delete(0)).thenReturn(true);
		when(roomManager.delete(57)).thenReturn(true);
		when(roomManager.delete(27)).thenReturn(true);
		
		roomService.removeUser(4);
		
		verify(messageSender, times(1)).sendRoomMessage(eq(0), argThat((PublishableUserEscapeMessage message) -> 
				message.getUserId() == 4));
		verify(messageSender, times(1)).sendRoomMessage(eq(57), argThat((PublishableUserEscapeMessage message) -> 
				message.getUserId() == 4));
		verify(messageSender, never()).sendRoomMessage(eq(27), any());
	}

	@Test
	public void joinRoom_givenExistingRoomId_addsUserToRoom() {
		var mockCurrent = UserTestUtils.mockUser(3);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = RoomTestUtils.mockRoom(32);
		when(roomManager.find(32)).thenReturn(Optional.of(mockRoom));
		when(roomManager.countByUserId(3)).thenReturn(2);
		
		roomService.joinRoom(32);
		
		assertThat(mockRoom.containsUser(3)).isTrue();
	}
	
	@Test
	public void joinRoom_givenObserversAllowed_doesNotAddUserToGame() {
		var mockCurrent = UserTestUtils.mockUser(3);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = RoomTestUtils.mockRoom(32);
		when(roomManager.find(32)).thenReturn(Optional.of(mockRoom));
		when(roomManager.countByUserId(3)).thenReturn(0);
		
		roomService.joinRoom(32);
		
		var engine = mockRoom.getGameEngine();
		verify(engine, never()).addPlayer(3);
	}
	
	@Test
	public void joinRoom_givenObserversNotAllowed_addsUserToRoomAndGame() {
		var mockCurrent = UserTestUtils.mockUser(3);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.areObserversAllowed(false)
				.build();
		when(roomManager.find(32)).thenReturn(Optional.of(mockRoom));
		when(roomManager.countByUserId(3)).thenReturn(0);
		
		roomService.joinRoom(32);
		
		var engine = mockRoom.getGameEngine();
		verify(engine, times(1)).addPlayer(3);
	}
	
	@Test
	public void joinRoom_givenExistingRoomId_returnsDetailsDto() {
		var mockCurrent = UserTestUtils.mockUser(3);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = RoomTestUtils.mockRoom();
		when(roomManager.find(32)).thenReturn(Optional.of(mockRoom));
		var mockDto = RoomTestUtils.mockDetailsDto();
		when(roomConverter.convertDetailed(mockRoom)).thenReturn(mockDto);
		when(roomManager.countByUserId(3)).thenReturn(1);
		
		var dto = roomService.joinRoom(32);
		
		assertThat(dto).isEqualTo(mockDto);
	}
	
	@Test
	public void joinRoom_givenCurrentInMoreThan2Rooms_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(3);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = RoomTestUtils.mockRoom();
		when(roomManager.find(32)).thenReturn(Optional.of(mockRoom));
		when(roomManager.countByUserId(3)).thenReturn(3);
		
		assertThatExceptionOfType(BadOperationException.class)
				.isThrownBy(() -> roomService.joinRoom(32))
				.withMessage("Cannot join to more than 3 rooms at the same time");
	}
	
	@Test
	public void joinRoom_givenNonExistinRoomId_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(3);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		when(roomManager.find(32)).thenReturn(Optional.empty());
		
		assertThatExceptionOfType(ResourceNotFoundException.class)
				.isThrownBy(() -> roomService.joinRoom(32))
				.withMessage("Cannot find the room with the given id: 32");
	}
	
	@Test
	public void joinRoom_givenCurrentAsGuestAndGuestFreeRoom_throwsException() {
		var mockCurrent = UserTestUtils.mockGuest(-3);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.privacy(RoomPrivacy.GUEST_FREE)
				.build();
		when(roomManager.find(32)).thenReturn(Optional.of(mockRoom));
		
		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> roomService.joinRoom(32))
				.withMessage("The current user cannot join the room with id: 32");
	}
	
	@Test
	public void joinRoom_givenPrivateRoomAndCurrentInvited_addsUserToRoom() {
		var mockCurrent = UserTestUtils.mockUser(3);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.privacy(RoomPrivacy.PRIVATE)
				.invitation(new Invitation(126, 3))
				.build();
		when(roomManager.find(32)).thenReturn(Optional.of(mockRoom));
		
		roomService.joinRoom(32);
		
		assertThat(mockRoom.containsUser(3)).isTrue();
	}
	
	@Test
	public void joinRoom_givenPrivateRoomAndCurrentNotInvited_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(3);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.privacy(RoomPrivacy.PRIVATE)
				.build();
		when(roomManager.find(32)).thenReturn(Optional.of(mockRoom));
		
		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> roomService.joinRoom(32))
				.withMessage("The current user cannot join the room with id: 32");
	}
	
	@Test
	public void joinRoom_givenObserversNotAllowedRoomAndNotFullRoom_addsUser() {
		var mockCurrent = UserTestUtils.mockUser(3);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.areObserversAllowed(false)
				.maxPlayers(2)
				.playerId(126)
				.build();
		when(roomManager.find(32)).thenReturn(Optional.of(mockRoom));
		
		roomService.joinRoom(32);
		
		assertThat(mockRoom.containsUser(3)).isTrue();
	}
	
	@Test
	public void joinRoom_givenObserversNotAllowedRoomAndFullRoom_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(3);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.areObserversAllowed(false)
				.user(UserTestUtils.mockGuest(-5))
				.maxPlayers(2)
				.playerId(126)
				.playerId(-5)
				.build();
		when(roomManager.find(32)).thenReturn(Optional.of(mockRoom));
				
		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> roomService.joinRoom(32))
				.withMessage("The current user cannot join the room with id: 32");
	}
	
	@Test
	public void processMessage_givenChatMessageAndChatOn_returnsMessage() {
		var mockCurrent = UserTestUtils.mockUser(35);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.user(mockCurrent)
				.build();
		when(roomManager.find(25)).thenReturn(Optional.of(mockRoom));
		
		var message = roomService.processMessage(25, new ChatMessage("Text"));
		
		assertThat(message).isInstanceOf(PublishableChatMessage.class);
		var chatMessage = (PublishableChatMessage) message;
		assertThat(chatMessage.getUserId()).isEqualTo(35);
		assertThat(chatMessage.getText()).isEqualTo("Text");
	}
	
	@Test
	public void processMessage_givenChatMessageAndChatOff_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(35);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.host(mockCurrent)
				.chatPolicy(ChatPolicy.CHAT_OFF)
				.build();
		when(roomManager.find(25)).thenReturn(Optional.of(mockRoom));
				
		assertThatExceptionOfType(BadOperationException.class)
				.isThrownBy(() -> roomService.processMessage(25, new ChatMessage("Text")))
				.withMessage("Cannot send a chat message because the chat in the room is off");
	}
	
	@Test
	public void processMessage_givenChatMessageAndCurrentAsPlayerAndChatOnlyForPlayers_returnsMessage() {
		var mockCurrent = UserTestUtils.mockUser(35);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.host(mockCurrent)
				.playerId(35)
				.chatPolicy(ChatPolicy.PLAYERS_ONLY)
				.build();
		when(roomManager.find(25)).thenReturn(Optional.of(mockRoom));
		
		var message = roomService.processMessage(25, new ChatMessage("Text"));
		
		assertThat(message).isInstanceOf(PublishableChatMessage.class);
		var chatMessage = (PublishableChatMessage) message;
		assertThat(chatMessage.getUserId()).isEqualTo(35);
		assertThat(chatMessage.getText()).isEqualTo("Text");
	}
	
	@Test
	public void processMessage_givenChatMessageAndCurrentAsObserverAndChatOnlyForPlayers_returnsMessage() {
		var mockCurrent = UserTestUtils.mockUser(35);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.host(mockCurrent)
				.chatPolicy(ChatPolicy.PLAYERS_ONLY)
				.build();
		when(roomManager.find(25)).thenReturn(Optional.of(mockRoom));
		
		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> roomService.processMessage(25, new ChatMessage("Text")))
				.withMessage("Current player is not allowed to send chat messages to the room");
	}
	
	@Test
	public void processMessage_givenChatMessageAndCurrentAsObserversAndChatOnlyForObservers_returnsMessage() {
		var mockCurrent = UserTestUtils.mockUser(35);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.host(mockCurrent)
				.chatPolicy(ChatPolicy.OBSERVERS_ONLY)
				.build();
		when(roomManager.find(25)).thenReturn(Optional.of(mockRoom));
		
		var message = roomService.processMessage(25, new ChatMessage("Text"));
				
		assertThat(message).isInstanceOf(PublishableChatMessage.class);
		var chatMessage = (PublishableChatMessage) message;
		assertThat(chatMessage.getUserId()).isEqualTo(35);
		assertThat(chatMessage.getText()).isEqualTo("Text");
	}
	
	@Test
	public void processMessage_givenChatMessageAndCurrentAsPlayerChatOnlyForObservers_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(35);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.host(mockCurrent)
				.playerId(35)
				.chatPolicy(ChatPolicy.OBSERVERS_ONLY)
				.build();
		when(roomManager.find(25)).thenReturn(Optional.of(mockRoom));
				
		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> roomService.processMessage(25, new ChatMessage("Text")))
				.withMessage("Current player is not allowed to send chat messages to the room");
	}
	
	@Test
	public void processMessage_givenInvitationMessage_addsInvitation() {
		var mockCurrent = UserTestUtils.mockUser(5);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		when(userService.find(26)).thenReturn(Optional.of(UserTestUtils.mockUser(26)));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.user(mockCurrent)
				.build();
		when(roomManager.find(25)).thenReturn(Optional.of(mockRoom));
				
		roomService.processMessage(25, new InvitationMessage(26));
		
		assertThat(mockRoom.getInvitations()).hasSize(1);
		var invitation = mockRoom.getInvitations().iterator().next();
		assertThat(invitation.getSenderId()).isEqualTo(5);
		assertThat(invitation.getInviteeId()).isEqualTo(26);
	}
	
	@Test
	public void processMessage_givenInvitationMessage_returnsMessage() {
		var mockCurrent = UserTestUtils.mockUser(5);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		when(userService.find(26)).thenReturn(Optional.of(UserTestUtils.mockUser(26)));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.user(mockCurrent)
				.build();
		when(roomManager.find(25)).thenReturn(Optional.of(mockRoom));
				
		var message = roomService.processMessage(25, new InvitationMessage(26));
		
		assertThat(message).isInstanceOf(PublishableInvitationMessage.class);
		var invitationMessage = (PublishableInvitationMessage) message;
		assertThat(invitationMessage.getUserId()).isEqualTo(5);
		assertThat(invitationMessage.getInviteeId()).isEqualTo(26);
	}
	
	@Test
	public void processMessage_givenInvitationMessageAndNoCurrent_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(5);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = RoomTestUtils.mockRoom();
		when(roomManager.find(25)).thenReturn(Optional.of(mockRoom));
				
		assertThatExceptionOfType(BadOperationException.class)
				.isThrownBy(() -> roomService.processMessage(25, new InvitationMessage(26)))
				.withMessage("Cannot invite the user with id: 26; cannot find the invited user");
	}
	
	@Test
	public void processMessage_givenInvitationMessageAndCurrentNotAsHostPrivateRoom_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(5);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.user(mockCurrent)
				.privacy(RoomPrivacy.PRIVATE)
				.build();
		when(roomManager.find(25)).thenReturn(Optional.of(mockRoom));
		when(userService.find(26)).thenReturn(Optional.of(UserTestUtils.mockUser(26)));
				
		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> roomService.processMessage(25, new InvitationMessage(26)))
				.withMessage("The current user is not a host of the room");
	}
	
	@Test
	public void processMessage_givenInvitationMessageCurrentAsGuest_throwsException() {
		var mockCurrent = UserTestUtils.mockGuest(-6);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.user(mockCurrent)
				.build();
		when(roomManager.find(25)).thenReturn(Optional.of(mockRoom));
		when(userService.find(26)).thenReturn(Optional.of(UserTestUtils.mockUser(26)));
				
		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> roomService.processMessage(25, new InvitationMessage(26)))
				.withMessage("Guests cannot invite other users");
	}
	
	@Test
	public void processMessage_givenInvitationMessageAndAlreadyInvitedUserId_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(5);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.user(mockCurrent)
				.invitation(new Invitation(126, 26))
				.build();
		when(roomManager.find(25)).thenReturn(Optional.of(mockRoom));
		when(userService.find(26)).thenReturn(Optional.of(UserTestUtils.mockUser(26)));
				
		assertThatExceptionOfType(BadOperationException.class)
				.isThrownBy(() -> roomService.processMessage(25, new InvitationMessage(26)))
				.withMessage("Cannot invite the user with id: 26; the user is already invited");
	}
	
	@Test
	public void processMessage_givenGameChangeMessageAndCurrentAsHost_changesGameEngine() {
		var mockCurrent = UserTestUtils.mockUser(126);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.game(Game.CHESS)
				.build();
		when(roomManager.find(12)).thenReturn(Optional.of(mockRoom));
		var newSettings = new DontGetAngryGameSettingsDto(48, 4, 1000);
		var mockEngine = GameEngineTestUtils.mockEngine();
		when(engineFactory.create(newSettings)).thenReturn(mockEngine);
		
		roomService.processMessage(12, new GameChangeMessage(newSettings));
		
		assertThat(mockRoom.getGameEngine()).isEqualTo(mockEngine);
	}
	
	@Test
	public void processMessage_givenGameChangeMessageAndCurrentAsHost_returnsMessage() {
		var mockCurrent = UserTestUtils.mockUser(126);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.game(Game.CHESS)
				.build();
		when(roomManager.find(12)).thenReturn(Optional.of(mockRoom));
		var newSettings = new DontGetAngryGameSettingsDto(48, 4, 1000);
		var mockEngine = GameEngineTestUtils.mockEngine();
		when(engineFactory.create(newSettings)).thenReturn(mockEngine);
		
		var message = roomService.processMessage(12, new GameChangeMessage(newSettings));
		
		assertThat(message).isInstanceOf(PublishableGameChangeMessage.class);
		var gameChangeMessage = (PublishableGameChangeMessage) message;
		assertThat(gameChangeMessage.getNewSettings()).isEqualTo(newSettings);
	}
	
	@Test
	public void processMessage_givenGameChangeMessageAndCurrentNotAsHost_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(16);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.game(Game.CHESS)
				.user(mockCurrent)
				.build();
		when(roomManager.find(12)).thenReturn(Optional.of(mockRoom));
		
		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> roomService.processMessage(12, new GameChangeMessage(
						new DontGetAngryGameSettingsDto(48, 4, 1000))))
				.withMessage("The current user is not a host of the room");
	}
	
	@Test
	public void processMessage_givenGameChangeMessageAndStartedGame_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(126);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.game(Game.CHESS)
				.gameStatus(GameStatus.STARTED)
				.build();
		when(roomManager.find(12)).thenReturn(Optional.of(mockRoom));
		var newSettings = new DontGetAngryGameSettingsDto(48, 4, 1000);
		var mockEngine = GameEngineTestUtils.mockEngine();
		when(engineFactory.create(newSettings)).thenReturn(mockEngine);
		
		assertThatExceptionOfType(BadOperationException.class)
				.isThrownBy(() -> roomService.processMessage(12, new GameChangeMessage(newSettings)))
				.withMessage("A game can be changed only when this is not started");
	}
	
	@Test
	public void processMessage_givenGameChangeMessageAndCountdownGameStatus_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(126);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.game(Game.CHESS)
				.gameStatus(GameStatus.COUNTDOWN)
				.build();
		when(roomManager.find(12)).thenReturn(Optional.of(mockRoom));
		var newSettings = new DontGetAngryGameSettingsDto(48, 4, 1000);
		var mockEngine = GameEngineTestUtils.mockEngine();
		when(engineFactory.create(newSettings)).thenReturn(mockEngine);
		
		assertThatExceptionOfType(BadOperationException.class)
				.isThrownBy(() -> roomService.processMessage(12, new GameChangeMessage(newSettings)))
				.withMessage("A game can be changed only when this is not started");
	}
	
	@Test
	public void processMessage_givenGameChangeMessageAndTheSameGame_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(126);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = RoomTestUtils.mockRoom();
		when(roomManager.find(12)).thenReturn(Optional.of(mockRoom));
		
		assertThatExceptionOfType(BadOperationException.class)
				.isThrownBy(() -> roomService.processMessage(12, new GameChangeMessage(
						new DontGetAngryGameSettingsDto(48, 4, 1000))))
				.withMessage("Cannot change a game; the same game is already set");
	}
	
	@Test
	public void processMessage_givenGameChangeMessageAndObserversNotAllowed_addsAllUsersToGame() {
		var mockCurrent = UserTestUtils.mockUser(126);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.game(Game.CHESS)
				.areObserversAllowed(false)
				.user(UserTestUtils.mockUser(135))
				.user(UserTestUtils.mockUser(136))
				.user(UserTestUtils.mockUser(137))
				.host(mockCurrent)
				.playerId(135)
				.playerId(136)
				.playerId(137)
				.playerId(126)
				.build();
		when(roomManager.find(12)).thenReturn(Optional.of(mockRoom));
		var newSettings = new DontGetAngryGameSettingsDto(48, 2, 1000);
		var mockEngine = GameEngineTestUtils.mockEngine();
		when(engineFactory.create(newSettings)).thenReturn(mockEngine);
		
		roomService.processMessage(12, new GameChangeMessage(newSettings));
		
		verify(mockEngine, times(1)).addPlayer(126);
		verify(mockEngine, times(1)).addPlayer(135);
	}
	
	@Test
	public void processMessage_givenGameChangeMessageAndObserversNotAllowed_removesOtherUsers() {
		var mockCurrent = UserTestUtils.mockUser(126);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.game(Game.CHESS)
				.areObserversAllowed(false)
				.user(UserTestUtils.mockUser(135))
				.user(UserTestUtils.mockUser(136))
				.user(UserTestUtils.mockUser(137))
				.host(mockCurrent)
				.playerId(135)
				.playerId(136)
				.playerId(137)
				.playerId(126)
				.build();
		when(roomManager.find(12)).thenReturn(Optional.of(mockRoom));
		var newSettings = new DontGetAngryGameSettingsDto(48, 2, 1000);
		var mockEngine = GameEngineTestUtils.mockEngine();
		when(mockEngine.containsPlayer(126)).thenReturn(true);
		when(mockEngine.containsPlayer(135)).thenReturn(true);
		when(engineFactory.create(newSettings)).thenReturn(mockEngine);
		
		roomService.processMessage(12, new GameChangeMessage(newSettings));
		
		var users = mockRoom.getUsers().stream()
				.map(user -> user.getId())
				.collect(Collectors.toList());
		assertThat(users).containsExactly(126, 135);
	}
	
	@Test
	public void processMessage_givenRoomSettingsChangeMessageAndCurrentAsHost_changesRoomSettings() {
		var mockCurrent = UserTestUtils.mockUser(126);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = RoomTestUtils.mockRoom();
		when(roomManager.find(12)).thenReturn(Optional.of(mockRoom));
		
		roomService.processMessage(12, new RoomSettingsChangeMessage(false, RoomPrivacy.GUEST_FREE,
				ChatPolicy.OBSERVERS_ONLY));
		
		assertThat(mockRoom.areObserversAllowed()).isFalse();
		assertThat(mockRoom.getPrivacy()).isEqualTo(RoomPrivacy.GUEST_FREE);
		assertThat(mockRoom.getChatPolicy()).isEqualTo(ChatPolicy.OBSERVERS_ONLY);
	}
	
	@Test
	public void processMessage_givenRoomSettingsChangeMessageAndOnlyNewChatPolicyCurrentNotAsHost_changesChatPolicy() {
		var mockCurrent = UserTestUtils.mockUser(126);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = RoomTestUtils.mockRoom();
		when(roomManager.find(12)).thenReturn(Optional.of(mockRoom));
		
		roomService.processMessage(12, new RoomSettingsChangeMessage(null, null, ChatPolicy.PLAYERS_ONLY));
		
		assertThat(mockRoom.areObserversAllowed()).isTrue();
		assertThat(mockRoom.getPrivacy()).isEqualTo(RoomPrivacy.PUBLIC);
		assertThat(mockRoom.getChatPolicy()).isEqualTo(ChatPolicy.PLAYERS_ONLY);
	}
	
	@Test
	public void processMessage_givenRoomSettingsChangeMessageAndCurrentAsHost_returnsMessage() {
		var mockCurrent = UserTestUtils.mockUser(126);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = RoomTestUtils.mockRoom();
		when(roomManager.find(12)).thenReturn(Optional.of(mockRoom));
		
		var message = roomService.processMessage(12, new RoomSettingsChangeMessage(false, RoomPrivacy.GUEST_FREE,
				ChatPolicy.OBSERVERS_ONLY));
		
		assertThat(message).isInstanceOf(PublishableRoomSettingsChangeMessage.class);
		var roomSettingsChangeMessage = (PublishableRoomSettingsChangeMessage) message;
		assertThat(roomSettingsChangeMessage.areObserversAllowed()).isFalse();
		assertThat(roomSettingsChangeMessage.getPrivacy()).isEqualTo(RoomPrivacy.GUEST_FREE);
		assertThat(roomSettingsChangeMessage.getChatPolicy()).isEqualTo(ChatPolicy.OBSERVERS_ONLY);
	}
	
	@Test
	public void processMessage_givenRoomSettingsChangeMessageAndCurrentNotAsHost_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(56);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = RoomTestUtils.mockRoom();
		when(roomManager.find(12)).thenReturn(Optional.of(mockRoom));
		
		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> roomService.processMessage(12, new RoomSettingsChangeMessage(false,
						RoomPrivacy.GUEST_FREE, ChatPolicy.OBSERVERS_ONLY)))
				.withMessage("The current user is not a host of the room");
	}
	
	@Test
	public void processMessage_givenRoomSettingsChangeMessageAndObserversDenied_addsAllUsersToGame() {
		var mockCurrent = UserTestUtils.mockUser(126);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.maxPlayers(3)
				.user(UserTestUtils.mockUser(50))
				.user(UserTestUtils.mockUser(51))
				.user(UserTestUtils.mockUser(52))
				.user(UserTestUtils.mockUser(53))
				.host(mockCurrent)
				.playerId(53)
				.build();
		when(roomManager.find(12)).thenReturn(Optional.of(mockRoom));
		var mockEngine = mockRoom.getGameEngine();
		
		roomService.processMessage(12, new RoomSettingsChangeMessage(false, null, null));
		
		verify(mockEngine, times(1)).addPlayer(126);
		verify(mockEngine, times(1)).addPlayer(50);
	}
	
	@Test
	public void processMessage_givenRoomSettingsChangeMessageAndObserversDenied_removesUsers() {
		var mockCurrent = UserTestUtils.mockUser(126);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.maxPlayers(3)
				.user(UserTestUtils.mockGuest(-50))
				.user(UserTestUtils.mockUser(51))
				.user(UserTestUtils.mockUser(52))
				.user(UserTestUtils.mockUser(53))
				.user(UserTestUtils.mockUser(54))
				.user(UserTestUtils.mockGuest(-55))
				.host(mockCurrent)
				.playerId(53)
				.build();
		var mockEngine = mockRoom.getGameEngine();
		when(mockEngine.containsPlayer(126)).thenReturn(true);
		when(mockEngine.containsPlayer(-50)).thenReturn(true);
		when(roomManager.find(12)).thenReturn(Optional.of(mockRoom));
		
		roomService.processMessage(12, new RoomSettingsChangeMessage(false, null, null));
		
		var usersIds = mockRoom.getUsers().stream()
				.map(user -> user.getId())
				.collect(Collectors.toList());
		assertThat(usersIds).containsExactly(126, -50, 53);
	}
	
	@Test
	public void processMessage_givenHostChangeMessageAndCurrentAsHost_changesHost() {
		var mockCurrent = UserTestUtils.mockUser(126);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.user(UserTestUtils.mockUser(32))
				.build();
		when(roomManager.find(10)).thenReturn(Optional.of(mockRoom));
		
		roomService.processMessage(10, new HostChangeMessage(32));
		
		assertThat(mockRoom.getHost().getId()).isEqualTo(32);
	}
	
	@Test
	public void processMessage_givenHostChangeMessageAndCurrentAsHost_shiftsUsers() {
		var mockCurrent = UserTestUtils.mockUser(126);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.user(UserTestUtils.mockUser(32))
				.user(UserTestUtils.mockGuest(-33))
				.user(UserTestUtils.mockGuest(-34))
				.build();
		when(roomManager.find(10)).thenReturn(Optional.of(mockRoom));
		
		roomService.processMessage(10, new HostChangeMessage(-33));
		
		assertThat(mockRoom.getUsers().get(1)).isEqualTo(mockCurrent);
	}
	
	@Test
	public void processMessage_givenHostChangeMessageAndCurrentNotAsHost_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(32);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.user(UserTestUtils.mockUser(32))
				.user(UserTestUtils.mockUser(33))
				.build();
		when(roomManager.find(10)).thenReturn(Optional.of(mockRoom));
		
		assertThatExceptionOfType(AccessDeniedException.class)
				.isThrownBy(() -> roomService.processMessage(10, new HostChangeMessage(33)))
				.withMessage("The current user is not a host of the room");
	}
	
	@Test
	public void processMessage_givenHostChangeMessageAndUserNotPresent_throwsException() {
		var mockCurrent = UserTestUtils.mockUser(126);
		when(userService.findCurrent()).thenReturn(Optional.of(mockCurrent));
		var mockRoom = (new RoomTestUtils.MockRoomBuilder())
				.user(UserTestUtils.mockUser(32))
				.user(UserTestUtils.mockUser(33))
				.build();
		when(roomManager.find(10)).thenReturn(Optional.of(mockRoom));
		
		assertThatExceptionOfType(BadOperationException.class)
				.isThrownBy(() -> roomService.processMessage(10, new HostChangeMessage(212)))
				.withMessage("Cannot find the user with the given id: 212 in the room");
	}
}
