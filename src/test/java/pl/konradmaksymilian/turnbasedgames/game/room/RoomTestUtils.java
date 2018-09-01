package pl.konradmaksymilian.turnbasedgames.game.room;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.GameEngineTestUtils;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.GameResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameStatus;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.GameRoomDetailsResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.room.dto.GameRoomResponseDto;
import pl.konradmaksymilian.turnbasedgames.user.UserTestUtils;
import pl.konradmaksymilian.turnbasedgames.user.entity.User;

public class RoomTestUtils {
	
	private RoomTestUtils() {}

	public static GameRoom mockRoom() {
		return (new MockRoomBuilder()).build();
	}
	
	public static GameRoom mockRoom(int id) {
		return (new MockRoomBuilder())
				.id(id)
				.build();
	}
	
	public static GameRoomResponseDto mockDto() {
		return (new GameRoomResponseDto.Builder())
				.areObserversAllowed(true)
				.chatPolicy(ChatPolicy.CHAT_ON)
				.creationTime(Instant.now().toEpochMilli())
				.game(Game.DONT_GET_ANGRY)
				.id(1)
				.maxPlayers(4)
				.players(2)
				.users(Arrays.asList("nick0", "nick1", "nick2", "nick3"))
				.build();
	}
	
	public static GameRoomDetailsResponseDto mockDetailsDto() {
		var players = new HashMap<Integer, Integer>();
		players.put(0, 1);
		players.put(1, 5);
		
		return (new GameRoomDetailsResponseDto.Builder())
				.areObserversAllowed(true)
				.chatPolicy(ChatPolicy.CHAT_ON)
				.creationTime(Instant.now().toEpochMilli())
				.gameDto(new GameResponseDto(players, GameStatus.NOT_STARTED, 0) {
					@Override
					public Game getGame() {
						return Game.DONT_GET_ANGRY;
					}
				})
				.privacy(RoomPrivacy.PUBLIC)
				.id(1)
				.users(Arrays.asList("nick0", "nick1", "nick2", "nick3"))
				.build();
	}
	
	public static class MockRoomBuilder {
		private int id = 0;
		private Instant creationTime = Instant.now();
		private ChatPolicy chatPolicy = ChatPolicy.CHAT_ON;
		private RoomPrivacy privacy = RoomPrivacy.PUBLIC;
		private User host = UserTestUtils.mockUser(126);
		private List<User> users = new ArrayList<>();
		private Set<Invitation> invitations = new HashSet<>();
		private boolean areObserversAllowed = true;
		private Set<Integer> playersIds = new HashSet<>();
		private Game game = Game.DONT_GET_ANGRY;
		private int maxPlayers = 4;
		private GameStatus gameStatus = GameStatus.NOT_STARTED;

		public MockRoomBuilder id(int id) {
			this.id = id;
			return this;
		}

		public MockRoomBuilder creationTime(Instant creationTime) {
			this.creationTime = creationTime;
			return this;
		}

		public MockRoomBuilder chatPolicy(ChatPolicy chatPolicy) {
			this.chatPolicy = chatPolicy;
			return this;
		}

		public MockRoomBuilder privacy(RoomPrivacy privacy) {
			this.privacy = privacy;
			return this;
		}

		public MockRoomBuilder host(User host) {
			this.host = host;
			return this;
		}

		public MockRoomBuilder user(User user) {
			users.add(user);
			return this;
		}

		public MockRoomBuilder invitation(Invitation invitation) {
			invitations.add(invitation);
			return this;
		}

		public MockRoomBuilder areObserversAllowed(boolean areObserversAllowed) {
			this.areObserversAllowed = areObserversAllowed;
			return this;
		}

		public MockRoomBuilder playerId(int playerId) {
			playersIds.add(playerId);
			return this;
		}

		public MockRoomBuilder game(Game game) {
			this.game = game;
			return this;
		}

		public MockRoomBuilder maxPlayers(int maxPlayers) {
			this.maxPlayers = maxPlayers;
			return this;
		}
		
		public MockRoomBuilder gameStatus(GameStatus gameStatus) {
			this.gameStatus = gameStatus;
			return this;
		}
		
		public GameRoom build() {
			var mockEngine = (new GameEngineTestUtils.MockEngineBuilder())
					.game(game)
					.gameStatus(gameStatus)
					.maxPlayers(maxPlayers)
					.playersIds(playersIds)
					.build();
			
			var mockRoom = (new GameRoom.Builder())
					.chatPolicy(chatPolicy)
					.creationTime(creationTime)
					.host(host)
					.invitations(invitations)
					.observersAllowed(areObserversAllowed)
					.privacy(privacy)
					.gameEngine(mockEngine)
					.build();
			mockRoom.setId(id);
			mockRoom.getUsers().addAll(users);
			
			return mockRoom;
		}
	}
}
