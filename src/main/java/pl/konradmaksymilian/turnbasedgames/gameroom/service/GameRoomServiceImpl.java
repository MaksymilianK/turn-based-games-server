package pl.konradmaksymilian.turnbasedgames.gameroom.service;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import pl.konradmaksymilian.turnbasedgames.core.dto.PageResponseDto;
import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;
import pl.konradmaksymilian.turnbasedgames.core.exception.ResourceNotFoundException;
import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.GameCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameEngine;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameStatus;
import pl.konradmaksymilian.turnbasedgames.gameroom.ChatPolicy;
import pl.konradmaksymilian.turnbasedgames.gameroom.GameRoom;
import pl.konradmaksymilian.turnbasedgames.gameroom.Invitation;
import pl.konradmaksymilian.turnbasedgames.gameroom.RoomPrivacy;
import pl.konradmaksymilian.turnbasedgames.gameroom.converter.GameRoomToDtoConverter;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.GameRoomCreateDto;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.GameRoomDetailsResponseDto;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.GameRoomResponseDto;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.ChatMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.GameChangeMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.HostChangeMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.InvitationMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.PublishableChatMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.PublishableGameChangeMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.PublishableHostChangeMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.PublishableInvitationMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.PublishableRoomMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.PublishableRoomSettingsChangeMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.PublishableUserEscapeMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.PublishableUserJoinMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.PublishableUserKickMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.RoomMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.RoomSettingsChangeMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.dto.message.UserKickMessage;
import pl.konradmaksymilian.turnbasedgames.gameroom.repository.GameRoomManager;
import pl.konradmaksymilian.turnbasedgames.user.Role;
import pl.konradmaksymilian.turnbasedgames.user.entity.User;
import pl.konradmaksymilian.turnbasedgames.user.service.UserService;

@Service
public class GameRoomServiceImpl implements GameRoomService {

	private GameRoomManager roomManager;
	private UserService userService;
	private GameRoomToDtoConverter roomConverter;
	private RoomMessageSender messageSender;
	private GameEngineFactory engineFactory;
	
	public GameRoomServiceImpl(GameRoomManager roomManager, UserService userService, 
			GameRoomToDtoConverter roomConverter, RoomMessageSender messageSender, GameEngineFactory engineFactory) {
		this.roomManager = roomManager;
		this.userService = userService;
		this.roomConverter = roomConverter;
		this.messageSender = messageSender;
		this.engineFactory = engineFactory;
	}
	
	@Override
	public Optional<GameRoom> find(int id) {
		return roomManager.find(id);
	}

	@Override
	public PageResponseDto<GameRoomResponseDto> getPage(Pageable pageable, Game game) {
		return roomConverter.convertPage(roomManager.getPage(pageable, userService.findCurrent().get(), game));
	}

	@Override
	public int create(GameRoomCreateDto roomDto) {
		var current = userService.findCurrent().get();
		var now = Instant.now();
		
		throwExceptionIfUserInMoreThan2Rooms(current.getId());
		
		var room = roomManager.save((new GameRoom.Builder())
				.gameEngine(engineFactory.create(roomDto.getGameSettings()))
				.chatPolicy(roomDto.getChatPolicy())
				.invitations(roomDto.getInvitedUsers().stream()
						.map(userId -> new Invitation(current.getId(), userId, now))
						.collect(Collectors.toSet()))
				.observersAllowed(roomDto.areObserversAllowed())
				.privacy(roomDto.getPrivacy())
				.host(current)
				.build());
		
		return room.getId();
	}

	@Override
	public void removeUser(int userId) {
		var rooms = roomManager.findByUserId(userId);
		rooms.forEach(room -> {
			synchronized(room) {
				if (removeUserFromRoom(userId, room)) {
					messageSender.sendRoomMessage(room.getId(), new PublishableUserEscapeMessage(userId));
				} else {
					delete(room.getId());
				}
			}
		});
	}
	
	@Override
	public GameRoomDetailsResponseDto joinRoom(int roomId) {
		var room = getOrThrowException(roomId);
		var current = userService.findCurrent().get();

		throwExceptionIfUserInMoreThan2Rooms(current.getId());
		
		synchronized(room) {
			if (!canUserJoinRoom(current, room)) {
				throw new AccessDeniedException("The current user cannot join the room with id: " + roomId);
			} else {
				addUserToRoom(current, room);
				messageSender.sendRoomMessage(roomId, new PublishableUserJoinMessage(current.getId()));
			}
		}
		return roomConverter.convertDetailed(room);
	}
	
	@Override
	public PublishableRoomMessage processMessage(int roomId, RoomMessage message) {
		var room = getOrThrowException(roomId);
		var sender = userService.findCurrent().get();
		
		switch (message.getType()) {
			case CHAT_MESSAGE:
				return processChatMessage(room, sender, (ChatMessage) message);
			case INVITATION:
				return processInvitationMessage(room, sender, (InvitationMessage) message);
			case GAME_CHANGE:
				return processGameChangeMessage(room, sender, (GameChangeMessage) message);
			case ROOM_SETTINGS_CHANGE:
				return processRoomSettingsChangeMessage(room, sender, (RoomSettingsChangeMessage) message);
			case USER_KICK:
				return processUserKickMessage(room, sender, (UserKickMessage) message);
			case HOST_CHANGE:
				return processHostChangeMessage(room, sender, (HostChangeMessage) message);
			default:
				throw new GameRoomServiceException("Unknown message type");
		}
	}

	@Override
	public void deliverCommand(int roomId, GameCommand command) {
		var room = getOrThrowException(roomId);
		var current = userService.findCurrent().get();
		
		if (command.isHostCommand() && !current.equals(room.getHost())) {
			throw new AccessDeniedException("Only a host of a room could send this command");
		}
		
		command.setSenderId(current.getId());
		room.getGameEngine().processCommand(command);
	}
	
	private void throwExceptionIfUserInMoreThan2Rooms(int userId) {
		if (roomManager.countByUserId(userId) > 2) {
			throw new BadOperationException("Cannot join to more than 3 rooms at the same time");
		}
	}
	
	private boolean canUserJoinRoom(User user, GameRoom room) {
		boolean cannotAddPlayers = room.getGameEngine().getMaxPlayers() <= room.countUsers()
				|| !room.getGameEngine().getStatus().equals(GameStatus.NOT_STARTED);
		boolean cannotJoinPrivateRoom = room.getPrivacy().equals(RoomPrivacy.PRIVATE) 
				&& !room.isUserInvited(user.getId());
		boolean cannotJoinBecauseIsGuest = room.getPrivacy().equals(RoomPrivacy.GUEST_FREE)
				&& user.getRole().equals(Role.GUEST);
		
		if ((!room.areObserversAllowed() && cannotAddPlayers) || cannotJoinPrivateRoom || cannotJoinBecauseIsGuest) {
			return false;
		} else {
			return true;
		}
	}
	
	private void addUserToRoom(User user, GameRoom room) {
		room.addUser(user);
		if (!room.areObserversAllowed()) {
			room.getGameEngine().addPlayer(user.getId());
		}
	}
	
	private boolean removeUserFromRoom(int userId, GameRoom room) {		
		room.removeUser(userId);
		
		if (room.countUsers() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private void delete(int id) {
		if (!roomManager.delete(id)) {
			throw new GameRoomServiceException("Cannot delete the room with the given id: " + id);
		}
	}

	private PublishableChatMessage processChatMessage(GameRoom room, User sender, ChatMessage message) {
		synchronized (room) {
			var chatPolicy = room.getChatPolicy();
		
			if (chatPolicy.equals(ChatPolicy.CHAT_OFF)) {
				throw new BadOperationException("Cannot send a chat message because the chat in the room is off");
			} else if ((chatPolicy.equals(ChatPolicy.OBSERVERS_ONLY) && !room.isObserver(sender.getId()))
				|| (chatPolicy.equals(ChatPolicy.PLAYERS_ONLY) && room.isObserver(sender.getId()))) {
				throw new AccessDeniedException("Current player is not allowed to send chat messages to the room");
			}
		}	
		return new PublishableChatMessage(sender.getId(), message.getText());
	}
	
	private PublishableRoomMessage processInvitationMessage(GameRoom room, User sender, InvitationMessage message) {
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
			if (room.getPrivacy().equals(RoomPrivacy.PRIVATE)) {
				throwExceptionIfNotHost(room, sender);
			} else if (room.isUserInvited(invitedUserId)) {
				throw new BadOperationException("Cannot invite the user with id: " + invitedUserId + "; the user is"
						+ " already invited");
			}
		
			room.getInvitations().add(new Invitation(sender.getId(), invitedUserId, now));
		}
		return new PublishableInvitationMessage(now.toEpochMilli(), sender.getId(), invitedUserId);
	}
	
	private PublishableGameChangeMessage processGameChangeMessage(GameRoom room, User sender, GameChangeMessage message) {
		synchronized (room) {
			throwExceptionIfNotHost(room, sender);
	    	if (!room.getGameEngine().getStatus().equals(GameStatus.NOT_STARTED)) {
				throw new BadOperationException("A game can be changed only when this is not started");
			} else if (room.getGameEngine().getGame().equals(message.getNewSettings().getGame())) {
				throw new BadOperationException("Cannot change a game; the same game is already set");
			}
		
			room.setGameEngine(engineFactory.create(message.getNewSettings()));
			
			if (!room.areObserversAllowed()) {
	    		addAllUsersToGame(room);
	    		removeAllUsersNotInGame(room);
	    	}
		}
		return new PublishableGameChangeMessage(message.getNewSettings());
	}
	
	private PublishableRoomSettingsChangeMessage processRoomSettingsChangeMessage(GameRoom room, User sender,
			RoomSettingsChangeMessage message) {
		synchronized (room) {
			throwExceptionIfNotHost(room, sender);
		}
		
		if (message.areObserversAllowed() != null) {
			changeObserversAllowed(room, message.areObserversAllowed());
		}
		
		if (message.getPrivacy() != null) {
			room.setPrivacy(message.getPrivacy());
		}
		
		if (message.getChatPolicy() != null) {
			room.setChatPolicy(message.getChatPolicy());
		}
		
		return new PublishableRoomSettingsChangeMessage(message.areObserversAllowed(), message.getPrivacy(),
				message.getChatPolicy());
	}

	private PublishableUserKickMessage processUserKickMessage(GameRoom room, User sender, UserKickMessage message) {
		synchronized (room) {
			if (message.getKickedUserId() == sender.getId()) {
				throw new BadOperationException("Cannot kick self");
			} else if (!room.getHost().equals(sender) && sender.getRole().isLowerThan(Role.HELPER)) {
				throw new AccessDeniedException("Only a host of a room or a helper can kick a user");
			}
		
			room.removeUser(message.getKickedUserId());
		}		
		return new PublishableUserKickMessage(sender.getId(), message.getKickedUserId());
	}
	
	private PublishableRoomMessage processHostChangeMessage(GameRoom room, User sender, HostChangeMessage message) {
		throwExceptionIfNotHost(room, sender);
		var newHostId = message.getNewHostId();
		
		synchronized (room) {
			if (newHostId == room.getHost().getId()) {
				throw new BadOperationException("The current user is already a host");
			} else if (!room.containsUser(newHostId)) {
				throw new BadOperationException("Cannot find the user with the given id: " + newHostId + " in the room");
			}
			changeHost(room, newHostId);
		}
		return new PublishableHostChangeMessage(newHostId);
	}
	
	private void changeObserversAllowed(GameRoom room, boolean areObserversAllowed) {
		room.setObserversAllowed(areObserversAllowed);
		if (!room.areObserversAllowed()) {
			if (room.getGameEngine().getStatus().equals(GameStatus.NOT_STARTED)) {
				addAllUsersToGame(room);
			}
			removeAllUsersNotInGame(room);
		}
	}
	
	private void addAllUsersToGame(GameRoom room) {
		var userIterator = room.getUsers().iterator();
		var engine = room.getGameEngine();
		
		if (!engine.containsPlayer(room.getHost().getId())) {
			if (engine.getMaxPlayers() == engine.countPlayers()) {
				engine.removePlayer(engine.getPlayersUsersIds().iterator().next());
			}
			engine.addPlayer(room.getHost().getId());
		}
		
		userIterator.next(); //omit a host
		
		while (engine.getMaxPlayers() > engine.countPlayers() && userIterator.hasNext()) {
			var user = userIterator.next();
			if (!engine.containsPlayer(user.getId())) {
				engine.addPlayer(user.getId());
			}
		}
	}
	
	private void removeAllUsersNotInGame(GameRoom room) {
		var userIterator = room.getUsers().iterator();
		while (userIterator.hasNext()) {
			var user = userIterator.next();
			if (!room.getGameEngine().containsPlayer(user.getId())) {
				userIterator.remove();
			}
		}
	}
	
	private GameRoom getOrThrowException(int id) {
		 return find(id).orElseThrow(() -> 
		 		new ResourceNotFoundException("Cannot find the room with the given id: " + id));
	}
	
	private void throwExceptionIfNotHost(GameRoom room, User user) {
		if (!room.getHost().equals(user)) {
			throw new AccessDeniedException("The current user is not a host of the room");
		}
	}
	
	private void changeHost(GameRoom room, int newHostId) {
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
