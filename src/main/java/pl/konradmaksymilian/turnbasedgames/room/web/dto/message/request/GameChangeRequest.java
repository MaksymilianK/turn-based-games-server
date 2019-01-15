package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsRequestDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;

public final class GameChangeRequest extends HostOnlyRoomRequest {

	@NotNull
	@Valid
	private GameSettingsRequestDto settings;
	
	public GameChangeRequest(@JsonProperty("gameSettings") GameSettingsRequestDto settings) {
		this.settings = settings;
	}
	
	public GameSettingsRequestDto getSettings() {
		return settings;
	}

	@Override
	public int getCode() {
		return SharedRoomMessageName.GAME_CHANGE.code();
	}
}
