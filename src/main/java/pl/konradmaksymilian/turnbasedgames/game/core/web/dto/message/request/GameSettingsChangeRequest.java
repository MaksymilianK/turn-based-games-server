package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.SharedGameEventName;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsRequestDto;

public final class GameSettingsChangeRequest extends HostOnlyGameRequest {
	
	@Valid
	private final GameSettingsRequestDto newSettings;
	
	public GameSettingsChangeRequest(@JsonProperty("newSettings") GameSettingsRequestDto newSettings) {
		this.newSettings = newSettings;
	}
	
	public GameSettingsRequestDto getNewSettings() {
		return newSettings;
	}
	
	@Override
	public int getCode() {
		return SharedGameEventName.GAME_SETTINGS_CHANGE.code();
	}
}
