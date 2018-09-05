package pl.konradmaksymilian.turnbasedgames.gameroom.dto;

import java.time.Instant;
import java.util.List;

import pl.konradmaksymilian.turnbasedgames.core.exception.BuilderNullPropertyException;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.GameResponseDto;
import pl.konradmaksymilian.turnbasedgames.gameroom.ChatPolicy;
import pl.konradmaksymilian.turnbasedgames.gameroom.RoomPrivacy;

public class GameRoomDetailsResponseDto {

	private final int id;
	private final long creationTime;
	private final List<String> users;
	private final boolean areObserversAllowed;
	private final RoomPrivacy privacy;
	private final ChatPolicy chatPolicy;
	private final GameResponseDto gameDto;
	private final long now;

	/**
	 * @param now current time (millis from epoch)
	 */
	private GameRoomDetailsResponseDto(int id, long creationTime, List<String> users, boolean areObserversAllowed,
			RoomPrivacy privacy, ChatPolicy chatPolicy, GameResponseDto gameDto, long now) {
		this.id = id;
		this.creationTime = creationTime;
		this.users = users;
		this.areObserversAllowed = areObserversAllowed;
		this.privacy = privacy;
		this.chatPolicy = chatPolicy;
		this.gameDto = gameDto;
		this.now = now;
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

	public RoomPrivacy getPrivacy() {
		return privacy;
	}

	public ChatPolicy getChatPolicy() {
		return chatPolicy;
	}

	public GameResponseDto getGameDto() {
		return gameDto;
	}
	
	public long getNow() {
		return now;
	}

	public static class Builder {
		
		private Integer id;
		private Long creationTime;
		private List<String> users;
		private Boolean areObserversAllowed;
		private RoomPrivacy privacy;
		private ChatPolicy chatPolicy;
		private GameResponseDto gameDto;
		private Long now;
		
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
		
		public Builder gameDto(GameResponseDto gameDto) {
			this.gameDto = gameDto;
			return this;
		}
		
		public Builder now(long now) {
			this.now = now;
			return this;
		}
		
		public GameRoomDetailsResponseDto build() {
			if (id == null || creationTime == null || users == null || areObserversAllowed == null || privacy == null
					|| chatPolicy == null || gameDto == null) {
				throw new BuilderNullPropertyException(GameRoomResponseDto.class);
			} else if (now == null) {
				now = Instant.now().toEpochMilli();
			}
			
			return new GameRoomDetailsResponseDto(id, creationTime, users, areObserversAllowed, privacy, chatPolicy,
						gameDto, now);
		}
	}
}
