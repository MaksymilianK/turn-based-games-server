package pl.konradmaksymilian.turnbasedgames.gameroom.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;
import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameStatus;
import pl.konradmaksymilian.turnbasedgames.gameroom.GameRoom;
import pl.konradmaksymilian.turnbasedgames.gameroom.RoomPrivacy;
import pl.konradmaksymilian.turnbasedgames.user.Role;
import pl.konradmaksymilian.turnbasedgames.user.entity.User;

@Repository
public class GameRoomManager {

	private final Map<Integer, GameRoom> rooms = new HashMap<>();
	
	public Optional<GameRoom> find(int id) {
		return Optional.ofNullable(rooms.get(id));
	}
	
	public Page<GameRoom> getPage(Pageable pageable, User user, Game game) {
		var filteredRoomStream = filter(rooms.values().stream(), user, game);
		var filteredAndSortedRooms = sort(filteredRoomStream, pageable.getSort()).collect(Collectors.toList());
		long roomCounter = filteredAndSortedRooms.size();

		var rooms = filteredAndSortedRooms.stream()
				.skip(pageable.getOffset())
				.limit(pageable.getPageSize())
				.collect(Collectors.toList());
		
		
		return new PageImpl<>(rooms, pageable, roomCounter);
	}

	public GameRoom save(GameRoom room) {
		room.setId(generateId());
		rooms.put(room.getId(), room);
		return room;
	}
	
	public boolean delete(int id) {
		return rooms.remove(id) != null;
	}
	
	public Set<GameRoom> findByUserId(int userId) {
		return rooms.values().stream()
				.filter(room -> room.getUsers().stream().anyMatch(user -> user.getId() == userId))
				.collect(Collectors.toSet());
	}
	
	public int countByUserId(int userId) {
		return (int) rooms.values().stream()
				.filter(room -> room.getUsers().stream().anyMatch(user -> user.getId() == userId))
				.count();
	}
	
	private int generateId() {
		int id = 0;
		while (rooms.containsKey(id)) {
			id++;
		}
		return id;
	}
	
	private Stream<GameRoom> filter(Stream<GameRoom> roomStream, User user, Game game) {
		Stream<GameRoom> filteredRooms = roomStream;
		
		if (game != null) {
			filteredRooms = filteredRooms
					.filter(room -> room.getGameEngine().getGame().equals(game));
		}
		
		filteredRooms = filteredRooms
				.filter(room -> room.getGameEngine().getStatus().equals(GameStatus.NOT_STARTED))
				.filter(room -> room.areObserversAllowed() ? 
						true : room.getGameEngine().countPlayers() < room.getGameEngine().getMaxPlayers());
		
		if (user.getRole().equals(Role.GUEST)) {
			return filteredRooms.filter(room -> room.getPrivacy().equals(RoomPrivacy.PUBLIC));
		} else {
			return filteredRooms
					.filter(room -> !room.getPrivacy().equals(RoomPrivacy.PRIVATE) || room.isUserInvited(user.getId()));
		}
	}
	
	private Stream<GameRoom> sort(Stream<GameRoom> roomStream, Sort sort) {
		if (sort.isUnsorted()) {
			return sortByCreationTime(roomStream, Sort.Direction.ASC);
		} else {
			var iterator = sort.iterator();
			var order = iterator.next();
			if (iterator.hasNext()) {
				throw new BadOperationException("Cannot sort by more than one property");
			}
			
			switch (order.getProperty()) {
				case "id":
					return sortById(roomStream, order.getDirection());
				case "creationTime":
					return sortByCreationTime(roomStream, order.getDirection());
				case "users":
					return sortByUsers(roomStream, order.getDirection());
				case "players":
					return sortByPlayers(roomStream, order.getDirection());
				default:
					throw new BadOperationException("Cannot sort by the given property: '" + order.getProperty() + "'");
			}
		}
	}

	private Stream<GameRoom> sortByUsers(Stream<GameRoom> roomStream, Direction direction) {
		if (direction.equals(Sort.Direction.ASC)) {
			return roomStream.sorted(
					(room1, room2) -> room1.countUsers() - room2.countUsers());
		} else {
			return roomStream.sorted(
					(room1, room2) -> room2.countUsers() - room1.countUsers());
		}
	}

	private Stream<GameRoom> sortById(Stream<GameRoom> roomStream, Sort.Direction direction) {
		if (direction.equals(Sort.Direction.ASC)) {
			return roomStream.sorted((room1, room2) -> room1.getId() - room2.getId());
		} else {
			return roomStream.sorted((room1, room2) -> room2.getId() - room1.getId());
		}
	}
	
	private Stream<GameRoom> sortByCreationTime(Stream<GameRoom> roomStream, Sort.Direction direction) {
		if (direction.equals(Sort.Direction.ASC)) {
			return roomStream.sorted((room1, room2) -> room1.getCreationTime().compareTo(room2.getCreationTime()));
		} else {
			return roomStream.sorted((room1, room2) -> room2.getCreationTime().compareTo(room1.getCreationTime()));
		}
	}
	
	private Stream<GameRoom> sortByPlayers(Stream<GameRoom> roomStream, Sort.Direction direction) {
		if (direction.equals(Sort.Direction.ASC)) {
			return roomStream.sorted(
					(room1, room2) -> room1.getGameEngine().countPlayers() - room2.getGameEngine().countPlayers());
		} else {
			return roomStream.sorted(
					(room1, room2) -> room2.getGameEngine().countPlayers() - room1.getGameEngine().countPlayers());
		}
	}
}
