package pl.konradmaksymilian.turnbasedgames.game.core.action.command;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.core.action.SharedGameActionName;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.GameSettingsDto;

public final class GameSettingsChangeCommand extends HostGameCommand {
	
	@Valid
	private final GameSettingsDto newSettings;
	
	public GameSettingsChangeCommand(@JsonProperty("newSettings") GameSettingsDto newSettings) {
		this.newSettings = newSettings;
	}
	
	public GameSettingsDto getNewSettings() {
		return newSettings;
	}
	
	@Override
	public int getCode() {
		return SharedGameActionName.GAME_SETTINGS_CHANGE.code();
	}
}
