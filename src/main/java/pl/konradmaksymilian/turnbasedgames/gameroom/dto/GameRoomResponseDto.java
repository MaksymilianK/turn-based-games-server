package pl.konradmaksymilian.turnbasedgames.gameroom.dto;

import java.util.List;

import pl.konradmaksymilian.turnbasedgames.core.exception.BuilderNullPropertyException;
import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.gameroom.ChatPolicy;
import pl.konradmaksymilian.turnbasedgames.gameroom.RoomPrivacy;

public final class GameRoomResponseDto {

	private final int id;
	private final long creationTime;
	private final List<String> users;
	private final int players;
	private final int maxPlayers;	
	private final boolean areObserversAllowed;
	private final RoomPrivacy privacy;
	private final ChatPolicy chatPolicy;
	private final Game game;

	private GameRoomResponseDto(int id, long creationTime, List<String> users, int players, int maxPlayers,
			boolean areObserversAllowed, RoomPrivacy privacy, ChatPolicy chatPolicy, Game game) {
		this.id = id;
		this.creationTime = creationTime;
		this.users = users;
		this.players = players;
		this.maxPlayers = maxPlayers;
		this.areObserversAllowed = areObserversAllowed;
		this.privacy = privacy;
		this.chatPolicy = chatPolicy;
		this.game = game;
	}

	public boolean isAreObserversAllowed() {
		return areObserversAllowed;
	}

	public int getId() {
		return id;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public List<String> getUsers() {
		return users;
	}

	public int getPlayers() {
		return players;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public RoomPrivacy getPrivacy() {
		return privacy;
	}

	public ChatPolicy getChatPolicy() {
		return chatPolicy;
	}

	public Game getGame() {
		return game;
	}
	
	public static class Builder {
		
		private Integer id;
		private Long creationTime;
		private List<String> users;
		private Integer players;
		private Integer maxPlayers;	
		private Boolean areObserversAllowed;
		private RoomPrivacy privacy;
		private ChatPolicy chatPolicy;
		private Game game;
		
		public Builder id(int id) {
			this.id = id;
			return this;
		}
		
		public Builder creationTime(long creationTime) {
			this.creationTime = creationTime;
			return this;
		}
		
		public Builder users(List<String> users) {
			this.users = users;
			return this;
		}
		
		public Builder players(int players) {
			this.players = players;
			return this;
		}
		
		public Builder maxPlayers(int maxPlayers) {
			this.maxPlayers = maxPlayers;
			return this;
		}
		
		public Builder areObserversAllowed(boolean areObserversAllowed) {
			this.areObserversAllowed = areObserversAllowed;
			return this;
		}
		
		public Builder privacy(RoomPrivacy privacy) {
			this.privacy = privacy;
			return this;
		}
		
		public Builder chatPolicy(ChatPolicy chatPolicy) {
			this.chatPolicy = chatPolicy;
			return this;
		}
		
		public Builder game(Game game) {
			this.game = game;
			return this;
		}
		
		public GameRoomResponseDto build() {
			if (id == null || creationTime == null || users == null || players == null || maxPlayers == null 
					|| areObserversAllowed == null || privacy == null || chatPolicy == null || game == null) {
				throw new BuilderNullPropertyException(GameRoomResponseDto.class);
			} else {
				return new GameRoomResponseDto(id, creationTime, users, players, maxPlayers, areObserversAllowed,
						privacy, chatPolicy, game);
			}
		}
	}
}
