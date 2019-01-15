package pl.konradmaksymilian.turnbasedgames.room.web.dto;

import java.util.List;

import pl.konradmaksymilian.turnbasedgames.core.exception.BuilderNullPropertyException;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsResponseDto;

public final class RoomResponseDto {

	private final int id;
	private final RoomSettingsResponseDto roomSettings;
	private final GameSettingsResponseDto gameSettings;
	private final long creationTime;
	private final List<String> users;
	private final int players;
	private final boolean isNotStarted;

	private RoomResponseDto(int id, RoomSettingsResponseDto roomSettings, GameSettingsResponseDto gameSettings,
			long creationTime, List<String> users, int players, boolean isNotStarted) {
		this.id = id;
		this.roomSettings = roomSettings;
		this.creationTime = creationTime;
		this.users = users;
		this.players = players;
		this.gameSettings = gameSettings;
		this.isNotStarted = isNotStarted;
	}

	public int getId() {
		return id;
	}
	
	public RoomSettingsResponseDto getRoomSettings() {
		return roomSettings;
	}
	
	public GameSettingsResponseDto getGameSettings() {
		return gameSettings;
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

	public boolean isNotStarted() {
		return isNotStarted;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private Integer id;
		private RoomSettingsResponseDto roomSettings;
		private GameSettingsResponseDto gameSettings;
		private Long creationTime;
		private List<String> users;
		private Integer players;
		private Boolean isNotStarted;
		
		Builder() {}
		
		public Builder id(int id) {
			this.id = id;
			return this;
		}
		
		public Builder roomSettings(RoomSettingsResponseDto roomSettings) {
			this.roomSettings = roomSettings;
			return this;
		}
		
		public Builder gameSettings(GameSettingsResponseDto gameSettings) {
			this.gameSettings = gameSettings;
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
		
		public Builder isNotStarted(boolean isNotStarted) {
			this.isNotStarted = isNotStarted;
			return this;
		}
		
		public RoomResponseDto build() {
			if (id == null || roomSettings == null || gameSettings == null || creationTime == null || users == null 
					|| isNotStarted == null) {
				throw new BuilderNullPropertyException(RoomResponseDto.class);
			} else {
				return new RoomResponseDto(id, roomSettings, gameSettings, creationTime, users, players, isNotStarted);
			}
		}
	}
}
