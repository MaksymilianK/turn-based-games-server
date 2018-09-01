package pl.konradmaksymilian.turnbasedgames.game.room.repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameStatus;
import pl.konradmaksymilian.turnbasedgames.game.room.GameRoom;
import pl.konradmaksymilian.turnbasedgames.game.room.Invitation;
import pl.konradmaksymilian.turnbasedgames.game.room.RoomPrivacy;
import pl.konradmaksymilian.turnbasedgames.game.room.RoomTestUtils;
import pl.konradmaksymilian.turnbasedgames.user.UserTestUtils;

public class GameRoomManagerTest {

	private GameRoomManager roomManager;
	
	@BeforeEach
	public void setUp() {
		roomManager = new GameRoomManager();
	}
	
	@Test
	public void getPage_givenPage1AndSize5_returnsValidRoomPage() {
		var pageable = PageRequest.of(1, 5);
		var mockCurrent = UserTestUtils.mockUser(5);
		for (int i = 0; i < 15; i++) {
			roomManager.save(RoomTestUtils.mockRoom());
		}
		
		var page = roomManager.getPage(pageable, mockCurrent, null);
		var content = page.getContent();
		
		assertThat(page.getNumber()).isEqualTo(1);
		assertThat(page.getTotalElements()).isEqualTo(15);
		assertThat(content).hasSize(5);
		assertThat(content).allMatch(room -> room.getId() > 4 && room.getId() < 10);
	}
	
	@Test
	public void getPage_givenSortByUsers_returnsSortedRoomPage() {
		var pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "users"));
		var mockCurrent = UserTestUtils.mockUser(5);
		var rooms = Arrays.asList(
				new RoomTestUtils.MockRoomBuilder()
						.id(0)
						.user(UserTestUtils.mockUser(1))
						.build(),
				new RoomTestUtils.MockRoomBuilder()
						.id(1)
						.build(),
				new RoomTestUtils.MockRoomBuilder()
						.id(2)
						.user(UserTestUtils.mockUser(1))
						.user(UserTestUtils.mockUser(2))
						.build()
				);
		rooms.forEach(room -> roomManager.save(room));
		
		var content = roomManager.getPage(pageable, mockCurrent, Game.DONT_GET_ANGRY).getContent();
		
		assertThat(content.get(0)).isEqualTo(rooms.get(2));
		assertThat(content.get(1)).isEqualTo(rooms.get(0));
		assertThat(content.get(2)).isEqualTo(rooms.get(1));
	}
	
	@Test
	public void getPage_givenDontGetAngryGame_returnsFilteredRoomPage() {
		var pageable = PageRequest.of(0, 10);
		var mockCurrent = UserTestUtils.mockUser(25);
		var rooms = Arrays.asList(
		        (new RoomTestUtils.MockRoomBuilder())
						.areObserversAllowed(false)
						.playerId(126)
						.playerId(5)
						.maxPlayers(2)
						.user(UserTestUtils.mockUser(5))
						.build(),
				(new RoomTestUtils.MockRoomBuilder())
						.privacy(RoomPrivacy.PRIVATE)
						.invitation(new Invitation(126, 25))
						.build(),
				(new RoomTestUtils.MockRoomBuilder())
						.privacy(RoomPrivacy.PRIVATE)
						.build(),
				(new RoomTestUtils.MockRoomBuilder())
						.privacy(RoomPrivacy.GUEST_FREE)
						.build(),
				(new RoomTestUtils.MockRoomBuilder())
						.game(Game.CHESS)
						.build(),
				(new RoomTestUtils.MockRoomBuilder())
						.areObserversAllowed(false)
						.playerId(126)
						.maxPlayers(2)
						.build(),
				(new RoomTestUtils.MockRoomBuilder())
						.gameStatus(GameStatus.COUNTDOWN)
						.build(),
				(new RoomTestUtils.MockRoomBuilder())
						.gameStatus(GameStatus.STARTED)
						.build(),
				(new RoomTestUtils.MockRoomBuilder())
						.areObserversAllowed(false)
						.gameStatus(GameStatus.COUNTDOWN)
						.build(),
				(new RoomTestUtils.MockRoomBuilder())
						.areObserversAllowed(false)
						.gameStatus(GameStatus.STARTED)
						.build()
		);
		rooms.forEach(room -> roomManager.save(room));
		
		var content = roomManager.getPage(pageable, mockCurrent, Game.DONT_GET_ANGRY).getContent();
		
		for (GameRoom room : content) {
			System.out.print(room.getId());
		}
		
		assertThat(content).hasSize(3);
		assertThat(content).contains(rooms.get(1), rooms.get(3), rooms.get(5));
	}
	
	@Test
	public void save_givenRoom_saves() {
		var mockRoom = RoomTestUtils.mockRoom(5);
		
		roomManager.save(mockRoom);
	
		var roomOptional = roomManager.find(0);
		assertThat(roomOptional).isPresent();
		assertThat(roomOptional.get()).isEqualTo(mockRoom);
	}
	
	@Test
	public void save_given7Rooms_setsRoomsIds() {		
		for (int i = 0; i < 7; i++) {
			roomManager.save(RoomTestUtils.mockRoom(5));
		}
	
		assertThat(roomManager.find(0).get().getId()).isEqualTo(0);
		assertThat(roomManager.find(1).get().getId()).isEqualTo(1);
		assertThat(roomManager.find(2).get().getId()).isEqualTo(2);
		assertThat(roomManager.find(3).get().getId()).isEqualTo(3);
		assertThat(roomManager.find(4).get().getId()).isEqualTo(4);
		assertThat(roomManager.find(5).get().getId()).isEqualTo(5);
		assertThat(roomManager.find(6).get().getId()).isEqualTo(6);
	}
	
	@Test
	public void save_givenVariousRooms_setsRoomIdTo3() {		
		for (int i = 0; i < 7; i++) {
			roomManager.save(RoomTestUtils.mockRoom(5));
		}
		roomManager.delete(3);
		var mockRoom = RoomTestUtils.mockRoom(5);
		roomManager.save(mockRoom);
	
		assertThat(mockRoom.getId()).isEqualTo(3);
	}
	
	@Test
	public void delete_givenExistingRoomId_deletesRoom() {		
		roomManager.save(RoomTestUtils.mockRoom(5));
		
		roomManager.delete(0);
		
		assertThat(roomManager.find(0)).isNotPresent();
	}
	
	@Test
	public void delete_givenExistingRoomId_returnsTrue() {		
		roomManager.save(RoomTestUtils.mockRoom(5));
	
		assertThat(roomManager.delete(0)).isTrue();
	}
	
	@Test
	public void delete_givenNonExistingRoomId_returnsFalse() {		
		roomManager.save(RoomTestUtils.mockRoom(5));
	
		assertThat(roomManager.delete(5)).isFalse();
	}
	
	@Test
	public void findByUserId_givenUserId_returnsRooms() {
		roomManager.save((new RoomTestUtils.MockRoomBuilder())
				.user(UserTestUtils.mockUser(5))
				.build());
		roomManager.save((new RoomTestUtils.MockRoomBuilder())
				.user(UserTestUtils.mockUser(4))
				.build());
		roomManager.save((new RoomTestUtils.MockRoomBuilder())
				.user(UserTestUtils.mockUser(5))
				.user(UserTestUtils.mockUser(6))
				.build());
		
		var rooms = roomManager.findByUserId(5);
		
		assertThat(rooms).hasSize(2);
	}
	
	@Test
	public void countByUserId_givenUserId_countsRooms() {
		roomManager.save((new RoomTestUtils.MockRoomBuilder())
				.user(UserTestUtils.mockUser(5))
				.build());
		roomManager.save((new RoomTestUtils.MockRoomBuilder())
				.user(UserTestUtils.mockUser(4))
				.build());
		roomManager.save((new RoomTestUtils.MockRoomBuilder())
				.user(UserTestUtils.mockUser(5))
				.user(UserTestUtils.mockUser(6))
				.build());
		roomManager.save((new RoomTestUtils.MockRoomBuilder())
				.user(UserTestUtils.mockUser(5))
				.build());
		roomManager.save((new RoomTestUtils.MockRoomBuilder())
				.user(UserTestUtils.mockUser(2))
				.build());
		roomManager.save((new RoomTestUtils.MockRoomBuilder())
				.user(UserTestUtils.mockUser(7))
				.user(UserTestUtils.mockUser(6))
				.build());
		
		var roomsNumber = roomManager.countByUserId(5);
		
		assertThat(roomsNumber).isEqualTo(3);
	}
}
