package pl.konradmaksymilian.turnbasedgames.room.data.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.GameStatus;
import pl.konradmaksymilian.turnbasedgames.game.core.data.Game;
import pl.konradmaksymilian.turnbasedgames.room.data.RoomPrivacy;
import pl.konradmaksymilian.turnbasedgames.room.data.entity.Room;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomsStatsResponseDto;
import pl.konradmaksymilian.turnbasedgames.user.data.Role;
import pl.konradmaksymilian.turnbasedgames.user.data.entity.User;

@Repository
public class RoomManager {

	private final Map<Integer, Room> rooms = new HashMap<>();
	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
	
	public Optional<Room> find(int id) {
		return Optional.ofNullable(rooms.get(id));
	}
	
	public RoomsStatsResponseDto getRoomsStats() {
		int[] allRooms = new int[Game.values().length];
		int[] allPlayers = new int[Game.values().length];
		
		rooms.values().forEach(room -> {
			allRooms[room.getGameEngine().getGame().ordinal()]++;
			allPlayers[room.getGameEngine().getGame().ordinal()] += room.getGameEngine().getPlayers().size();
		});
		
		var rooms = new HashMap<Game, Integer>();
		var players = new HashMap<Game, Integer>();
		for (Game game : Game.values()) {
			rooms.put(game, allRooms[game.ordinal()]);
			players.put(game, allPlayers[game.ordinal()]);
		}
		
		return new RoomsStatsResponseDto(rooms, players);
	}
	
	public Page<Room> getPage(Pageable pageable, User user, Game game) {
		var filteredRoomStream = filter(rooms.values().stream(), user, game);
		var filteredAndSortedRooms = sort(filteredRoomStream, pageable.getSort()).collect(Collectors.toList());
		long roomCounter = filteredAndSortedRooms.size();

		var rooms = filteredAndSortedRooms.stream()
				.skip(pageable.getOffset())
				.limit(pageable.getPageSize())
				.collect(Collectors.toList());
		
		return new PageImpl<>(rooms, pageable, roomCounter);
	}

	public int save(Room room) {
		room.setId(generateId());
		rooms.put(room.getId(), room);
		
		waitForHost(room.getId());
		
		return room.getId();
	}
	
	public boolean delete(int id) {
		return rooms.remove(id) != null;
	}
	
	public Set<Room> findByUserId(int userId) {
		return rooms.values().stream()
				.filter(room -> room.getUsers().stream().anyMatch(user -> user.getId() == userId))
				.collect(Collectors.toSet());
	}
	
	public int countByUserId(int userId) {
		return (int) rooms.values().stream()
				.filter(room -> room.getUsers().stream().anyMatch(user -> user.getId() == userId))
				.count();
	}
	
	private void waitForHost(int roomId) {
		executor.schedule(() -> {
			var room = rooms.get(roomId);
			if (room != null) {
				if (room.getUsers().size() == 0) {
					rooms.remove(roomId);
				}
			}
		}, 3, TimeUnit.SECONDS);
	}
	
	private int generateId() {
		int id = 0;
		while (rooms.containsKey(id)) {
			id++;
		}
		return id;
	}
	
	private Stream<Room> filter(Stream<Room> roomStream, User user, Game game) {
		Stream<Room> filteredRooms = roomStream;
		
		if (game != null) {
			filteredRooms = filteredRooms
					.filter(room -> room.getGameEngine().getGame().equals(game));
		}
		
		filteredRooms = filteredRooms
				.filter(room -> room.getGameEngine().getStatus().equals(GameStatus.NOT_STARTED))
				.filter(room -> room.getSettings().areObserversAllowed() ? true : room.getGameEngine().getPlayers().size()
						< room.getGameEngine().getMaxPlayers());
		
		if (user.getRole().equals(Role.GUEST)) {
			return filteredRooms.filter(room -> room.getSettings().getPrivacy().equals(RoomPrivacy.PUBLIC));
		} else {
			return filteredRooms
					.filter(room -> !room.getSettings().getPrivacy().equals(RoomPrivacy.PRIVATE) || room.getInvitations()
							.stream().anyMatch(invitation -> invitation.getInviteeId() == user.getId()));
		}
	}
	
	private Stream<Room> sort(Stream<Room> roomStream, Sort sort) {
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

	private Stream<Room> sortByUsers(Stream<Room> roomStream, Direction direction) {
		if (direction.equals(Sort.Direction.ASC)) {
			return roomStream.sorted(
					(room1, room2) -> room1.getUsers().size() - room2.getUsers().size());
		} else {
			return roomStream.sorted(
					(room1, room2) -> room2.getUsers().size() - room1.getUsers().size());
		}
	}

	private Stream<Room> sortById(Stream<Room> roomStream, Sort.Direction direction) {
		if (direction.equals(Sort.Direction.ASC)) {
			return roomStream.sorted((room1, room2) -> room1.getId() - room2.getId());
		} else {
			return roomStream.sorted((room1, room2) -> room2.getId() - room1.getId());
		}
	}
	
	private Stream<Room> sortByCreationTime(Stream<Room> roomStream, Sort.Direction direction) {
		if (direction.equals(Sort.Direction.ASC)) {
			return roomStream.sorted((room1, room2) -> room1.getCreationTime().compareTo(room2.getCreationTime()));
		} else {
			return roomStream.sorted((room1, room2) -> room2.getCreationTime().compareTo(room1.getCreationTime()));
		}
	}
	
	private Stream<Room> sortByPlayers(Stream<Room> roomStream, Sort.Direction direction) {
		if (direction.equals(Sort.Direction.ASC)) {
			return roomStream.sorted(
					(room1, room2) -> room1.getGameEngine().getPlayers().size() - 
							room2.getGameEngine().getPlayers().size());
		} else {
			return roomStream.sorted(
					(room1, room2) -> room2.getGameEngine().getPlayers().size() - 
							room1.getGameEngine().getPlayers().size());
		}
	}
}
