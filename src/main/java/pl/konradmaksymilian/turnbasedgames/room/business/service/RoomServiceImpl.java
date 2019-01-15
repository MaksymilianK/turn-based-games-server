package pl.konradmaksymilian.turnbasedgames.room.business.service;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import pl.konradmaksymilian.turnbasedgames.core.dto.PageResponseDto;
import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;
import pl.konradmaksymilian.turnbasedgames.core.exception.ResourceNotFoundException;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.GameStatus;
import pl.konradmaksymilian.turnbasedgames.game.core.business.factory.GameEngineFactoryFacade;
import pl.konradmaksymilian.turnbasedgames.game.core.data.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request.GameRequest;
import pl.konradmaksymilian.turnbasedgames.room.business.RoomMessageSenderFactory;
import pl.konradmaksymilian.turnbasedgames.room.business.converter.RoomResponseDtoConverter;
import pl.konradmaksymilian.turnbasedgames.room.data.ChatPolicy;
import pl.konradmaksymilian.turnbasedgames.room.data.RoomPrivacy;
import pl.konradmaksymilian.turnbasedgames.room.data.entity.Room;
import pl.konradmaksymilian.turnbasedgames.room.data.entity.Invitation;
import pl.konradmaksymilian.turnbasedgames.room.data.repository.RoomManager;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomCreateDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomResponseDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomsStatsResponseDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request.ChatMessageRequest;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request.GameChangeRequest;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request.HostChangeRequest;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request.InvitationRequest;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request.RoomRequest;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request.RoomSettingsChangeRequest;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request.UserKickRequest;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.ChatMessageResponse;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.GameChangeResponse;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.HostChangeResponse;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.InvitationResponse;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomDetailsResponse;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomResponse;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomSettingsChangeResponse;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.UserEscapeResponse;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.UserJoinResponse;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.UserKickResponse;
import pl.konradmaksymilian.turnbasedgames.user.business.service.UserService;
import pl.konradmaksymilian.turnbasedgames.user.data.Role;
import pl.konradmaksymilian.turnbasedgames.user.data.entity.User;

@Service
public class RoomServiceImpl implements RoomService {

	private final ScheduledExecutorService taskExecutor;
	private RoomManager roomManager;
	private UserService userService;
	private RoomResponseDtoConverter roomConverter;
	private RoomMessageSenderFactory messageSenderFactory;
	private GameEngineFactoryFacade engineFactoryFacade;
	
	public RoomServiceImpl(ScheduledExecutorService taskExecutor, RoomManager roomManager,
			UserService userService, RoomResponseDtoConverter roomConverter, 
			RoomMessageSenderFactory messageSenderFactory, GameEngineFactoryFacade engineFactoryFacade) {
		this.taskExecutor = taskExecutor;
		this.roomManager = roomManager;
		this.userService = userService;
		this.roomConverter = roomConverter;
		this.messageSenderFactory = messageSenderFactory;
		this.engineFactoryFacade = engineFactoryFacade;
	}
	
	@Autowired
	public RoomServiceImpl(RoomManager roomManager, UserService userService, 
			RoomResponseDtoConverter roomConverter, RoomMessageSenderFactory messageSenderFactory,
			GameEngineFactoryFacade engineFactoryFacade) {
		this(Executors.newScheduledThreadPool(5), roomManager, userService, roomConverter, messageSenderFactory,
				engineFactoryFacade);
	}
	
	@Override
	public RoomsStatsResponseDto getStats() {
		return roomManager.getRoomsStats();
	}
	
	@Override
	public Optional<Room> find(int id) {
		return roomManager.find(id);
	}

	@Override
	public PageResponseDto<RoomResponseDto> getPage(Pageable pageable, Game game) {
		return roomConverter.convertPage(roomManager.getPage(pageable, userService.findCurrent().get(), game));
	}

	@Override
	public int create(RoomCreateDto roomDto) {
		var current = userService.findCurrent().get();
		var now = Instant.now();
		
		throwExceptionIfUserInMoreThan2Rooms(current.getId());
		
		var room = Room.builder()
				.gameEngine(engineFactoryFacade.create(roomDto.getGameSettings()))
				.chatPolicy(roomDto.getChatPolicy())
				.invitations(roomDto.getInvitedUsers().stream()
						.map(userId -> new Invitation(current.getId(), userId, now))
						.collect(Collectors.toSet()))
				.observersAllowed(roomDto.areObserversAllowed())
				.privacy(roomDto.getPrivacy())
				.host(current)
				.build();
		var roomId = roomManager.save(room);
		
		room.injectMessageSender(messageSenderFactory.create(roomId));
		room.getGameEngine().injectMessageSender(room.getMessageSender());
		
		deleteIfHostHasNotJoined(room);
		
		return roomId;
	}

	@Override
	public void removeUser(int userId) {
		roomManager.findByUserId(userId).forEach(room -> {
			synchronized(room) {
				if (removeUserFromRoom(userId, room)) {
					room.getMessageSender().publish(new UserEscapeResponse(userId));
				} else {
					delete(room.getId());
				}
			}
		});
	}
	
	@Override
	public RoomDetailsResponse joinRoom(int roomId) {
		var room = getOrThrowException(roomId);
		var current = userService.findCurrent().get();

		throwExceptionIfUserInMoreThan2Rooms(current.getId());
		
		synchronized(room) {
			if (!canUserJoinRoom(current, room)) {
				throw new AccessDeniedException("The current user cannot join the room " + roomId);
			} else {
				addUserToRoom(current, room);
				room.getMessageSender().publish(new UserJoinResponse(current.getId()));
			}
		}
		return new RoomDetailsResponse(roomConverter.convertStatus(room), room.getGameEngine().getGameDetails());
	}
	
	@Override
	public void processMessage(int roomId, RoomRequest message) {
		var room = getOrThrowException(roomId);
		var sender = userService.findCurrent().get();
		
		if (message.isHostOnly() && !room.getHost().equals(sender)) {
			throw new AccessDeniedException("Only a host of a room can send the message " + message.getCode());
		}
		
		if (message.getCode() < 100) {
			processRoomRequest(room, sender, message);
		} else {
			processGameRequest(room, sender, (GameRequest) message);
		}
	}

	private void processRoomRequest(Room room, User sender, RoomRequest message) {
		RoomResponse response;
		
		if (message.getCode() == SharedRoomMessageName.CHAT_MESSAGE.code()) {
			response = processChatMessage(room, sender, (ChatMessageRequest) message);
		} else if (message.getCode() == SharedRoomMessageName.GAME_CHANGE.code()) {
			response = processGameChange(room, sender, (GameChangeRequest) message);
		} else if (message.getCode() == SharedRoomMessageName.HOST_CHANGE.code()) {
			response = processHostChange(room, sender, (HostChangeRequest) message);
		} else if (message.getCode() == SharedRoomMessageName.INVITATION.code()) {
			response = processInvitation(room, sender, (InvitationRequest) message);
		} else if (message.getCode() == SharedRoomMessageName.ROOM_SETTINGS_CHANGE.code()) {
			response = processRoomSettingsChange(room, sender, (RoomSettingsChangeRequest) message);
		} else if (message.getCode() == SharedRoomMessageName.USER_KICK.code()) {
			response = processUserKick(room, sender, (UserKickRequest) message);
		} else {
			throw new BadOperationException("Unrecognised message code " + message.getCode());
		}
		
		room.getMessageSender().publish(response);
	}
	
	private void processGameRequest(Room room, User sender, GameRequest message) {
		message.setSender(sender);
		room.getGameEngine().processMessage(message);
	}
	
	private void deleteIfHostHasNotJoined(Room room) {
		taskExecutor.schedule(() -> {
			if (!containsUser(room, room.getHost().getId())) {
				this.roomManager.delete(room.getId());
			}
		}, 5, TimeUnit.SECONDS);
	}
	
	private void throwExceptionIfUserInMoreThan2Rooms(int userId) {
		if (roomManager.countByUserId(userId) > 2) {
			throw new BadOperationException("Cannot join to more than 3 rooms at the same time");
		}
	}
	
	private boolean canUserJoinRoom(User user, Room room) {
		boolean cannotAddPlayers = room.getGameEngine().getMaxPlayers() <= room.getUsers().size()
				|| !room.getGameEngine().getStatus().equals(GameStatus.NOT_STARTED);
		boolean cannotJoinPrivateRoom = room.getSettings().getPrivacy().equals(RoomPrivacy.PRIVATE) 
				&& !isUserInvited(room, user.getId());
		boolean cannotJoinBecauseIsGuest = room.getSettings().getPrivacy().equals(RoomPrivacy.GUEST_FREE)
				&& user.getRole().equals(Role.GUEST);
		
		if ((!room.getSettings().areObserversAllowed() && cannotAddPlayers) || cannotJoinPrivateRoom 
				|| cannotJoinBecauseIsGuest) {
			return false;
		} else {
			return true;
		}
	}
	
	private void addUserToRoom(User user, Room room) {
		room.addUser(user);
		if (!room.getSettings().areObserversAllowed()) {
			room.getGameEngine().addPlayer(user.getNick());
		}
	}
	
	private boolean removeUserFromRoom(int userId, Room room) {		
		room.removeUser(userId);
		
		if (room.getUsers().size() > 0) {
			if (room.getHost() == null) {
				room.setHost(room.getUsers().get(0));
			}
			return true;
		} else {
			return false;
		}
	}
	
	private void delete(int id) {
		if (!roomManager.delete(id)) {
			throw new RoomServiceException("Cannot delete the room with the given id " + id);
		}
	}
	
	private boolean isUserInvited(Room room, int userId) {
		return room.getInvitations().stream().anyMatch(invitation -> invitation.getInviteeId() == userId);
	}
	
	private boolean containsUser(Room room, int userId) {
		return room.getUsers().stream().anyMatch(user -> user.getId() == userId);
	}
	
	private boolean isPlayer(Room room, String nick) {
		return room.getGameEngine().getPlayers().contains(nick);
	}
	
	private ChatMessageResponse processChatMessage(Room room, User sender, ChatMessageRequest message) {
		synchronized (room) {
			var chatPolicy = room.getSettings().getChatPolicy();
		
			if (chatPolicy.equals(ChatPolicy.CHAT_OFF)) {
				throw new BadOperationException("Cannot send a chat message because the chat in the room is off");
			} else if ((chatPolicy.equals(ChatPolicy.OBSERVERS_ONLY) && isPlayer(room, sender.getNick()) 
					|| (chatPolicy.equals(ChatPolicy.PLAYERS_ONLY) && !isPlayer(room, sender.getNick())))) {
				throw new AccessDeniedException("Current player is not allowed to send chat messages to the room");
			}
		}	
		return new ChatMessageResponse(sender.getId(), message.getText());
	}
	
	private InvitationResponse processInvitation(Room room, User sender, InvitationRequest message) {
		int invitedUserId = message.getInviteeId();
		var invitedUser = userService.find(invitedUserId);
		var now = Instant.now();

		if (!invitedUser.isPresent()) {
			throw new BadOperationException("Cannot invite the user with id: " + invitedUserId + "; cannot find the"
					+ " invited user");
		} else if (sender.getRole().equals(Role.GUEST)) {
			throw new AccessDeniedException("Guests cannot invite other users");
		}
		
		synchronized (room) {
			if (room.getSettings().getPrivacy().equals(RoomPrivacy.PRIVATE)) {
				throwExceptionIfNotHost(room, sender);
			} else if (isUserInvited(room, invitedUserId)) {
				throw new BadOperationException("Cannot invite the user with id: " + invitedUserId + "; the user is"
						+ " already invited");
			}
		
			room.getInvitations().add(new Invitation(sender.getId(), invitedUserId, now));
		}
		return new InvitationResponse(now.toEpochMilli(), sender.getId(), invitedUserId);
	}
	
	private GameChangeResponse processGameChange(Room room, User sender, GameChangeRequest message) {
		synchronized (room) {
			throwExceptionIfNotHost(room, sender);
	    	if (!room.getGameEngine().getStatus().equals(GameStatus.NOT_STARTED)) {
				throw new BadOperationException("A game can be changed only when this is not started");
			} else if (room.getGameEngine().getGame().equals(message.getSettings().getGame())) {
				throw new BadOperationException("Cannot change a game; the same game is already set");
			}
		
	    	var engine = engineFactoryFacade.create(message.getSettings());
			room.setGameEngine(engine);
			engine.injectMessageSender(room.getMessageSender());
			
			if (!room.getSettings().areObserversAllowed()) {
	    		addAllUsersToGame(room);
	    		removeAllUsersNotInGame(room);
	    	}
			return new GameChangeResponse(engine.getSettings());
		}
	}
	
	private RoomSettingsChangeResponse processRoomSettingsChange(Room room, User sender,
			RoomSettingsChangeRequest message) {
		synchronized (room) {
			throwExceptionIfNotHost(room, sender);
		}
		
		if (message.areObserversAllowed() != null) {
			changeObserversAllowed(room, message.areObserversAllowed());
		}
		
		if (message.getPrivacy() != null) {
			room.getSettings().setPrivacy(message.getPrivacy());
		}
		
		if (message.getChatPolicy() != null) {
			room.getSettings().setChatPolicy(message.getChatPolicy());
		}
		
		return new RoomSettingsChangeResponse(message.areObserversAllowed(), message.getPrivacy(),
				message.getChatPolicy());
	}

	private UserKickResponse processUserKick(Room room, User sender, UserKickRequest message) {
		synchronized (room) {
			if (message.getKickedUserId() == sender.getId()) {
				throw new BadOperationException("Cannot kick self");
			} else if (!room.getHost().equals(sender) && sender.getRole().isLowerThan(Role.HELPER)) {
				throw new AccessDeniedException("Only a host of a room or a helper can kick a user");
			}
		
			room.removeUser(message.getKickedUserId());
		}		
		return new UserKickResponse(sender.getId(), message.getKickedUserId());
	}
	
	private HostChangeResponse processHostChange(Room room, User sender, HostChangeRequest message) {
		throwExceptionIfNotHost(room, sender);
		var newHostId = message.getNewHostId();
		
		synchronized (room) {
			if (newHostId == room.getHost().getId()) {
				throw new BadOperationException("The current user is already a host");
			} else if (!containsUser(room, newHostId)) {
				throw new BadOperationException("Cannot find the user with the given id: " + newHostId + " in the room");
			}
			changeHost(room, newHostId);
		}
		return new HostChangeResponse(newHostId);
	}
	
	private void changeObserversAllowed(Room room, boolean areObserversAllowed) {
		room.getSettings().setObserversAllowed(areObserversAllowed);
		if (!room.getSettings().areObserversAllowed()) {
			if (room.getGameEngine().getStatus().equals(GameStatus.NOT_STARTED)) {
				addAllUsersToGame(room);
			}
			removeAllUsersNotInGame(room);
		}
	}
	
	private void addAllUsersToGame(Room room) {
		var userIterator = room.getUsers().iterator();
		var engine = room.getGameEngine();
		var players = engine.getPlayers();
		
		if (players.stream().noneMatch(nick -> nick.equals(room.getHost().getNick()))) {
			if (engine.getMaxPlayers() == players.size()) {
				engine.removePlayer(players.iterator().next());
			}
			engine.addPlayer(room.getHost().getNick());
		}
				
		while (engine.getMaxPlayers() > players.size() && userIterator.hasNext()) {
			var user = userIterator.next();
			if (players.stream().noneMatch(nick -> nick.equals(user.getNick()))) {
				engine.addPlayer(user.getNick());
			}
		}
	}
	
	private void removeAllUsersNotInGame(Room room) {
		var userIterator = room.getUsers().iterator();
		while (userIterator.hasNext()) {
			var user = userIterator.next();
			if (!room.getGameEngine().getPlayers().stream().noneMatch(nick -> nick.equals(user.getNick()))) {
				userIterator.remove();
			}
		}
	}
	
	private Room getOrThrowException(int id) {
		 return find(id).orElseThrow(() -> 
		 		new ResourceNotFoundException("Cannot find the room " + id));
	}
	
	private void throwExceptionIfNotHost(Room room, User user) {
		if (!room.getHost().equals(user)) {
			throw new AccessDeniedException("The current user is not a host of the room");
		}
	}
	
	private void changeHost(Room room, int newHostId) {
		var userIterator = room.getUsers().iterator();
		User newHost = null;
		
		while (userIterator.hasNext()) {
			newHost = userIterator.next();
			if (newHost.getId() == newHostId) {
				userIterator.remove();
			}
		}
		room.getUsers().add(0, newHost);
	}
}
