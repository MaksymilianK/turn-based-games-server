package pl.konradmaksymilian.turnbasedgames.gameroom.dto.message;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.GameSettingsDto;

public final class GameChangeMessage implements RoomMessage {

	@NotNull
	@Valid
	private GameSettingsDto newSettings;
	
	public GameChangeMessage(@JsonProperty("gameSettings") GameSettingsDto newSettings) {
		this.newSettings = newSettings;
	}
	
	public GameSettingsDto getNewSettings() {
		return newSettings;
	}

	@Override
	public RoomMessageType getType() {
		return RoomMessageType.GAME_CHANGE;
	}
}
