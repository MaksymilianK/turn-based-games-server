package pl.konradmaksymilian.turnbasedgames.game.room.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.GameSettingsDto;

public abstract class PlayerTimeLimitedGameSettings implements GameSettingsDto {

	@Min(30)
	@Max(7200)
	private final int playerTime;

	public PlayerTimeLimitedGameSettings(int playerTime) {
		this.playerTime = playerTime;
	}

	public int getPlayerTime() {
		return playerTime;
	}
}
