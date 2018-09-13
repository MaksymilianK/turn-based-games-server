package pl.konradmaksymilian.turnbasedgames.game.core.dto.event;

import pl.konradmaksymilian.turnbasedgames.game.core.action.SharedGameActionName;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.GameSettingsDto;

public final class GameSettingsChangeEventDto extends GameEventDto {

	private final GameSettingsDto newSettings;
	
	/**
 	* @param time the message sending time (millis from epoch)
 	*/
	public GameSettingsChangeEventDto(long time, GameSettingsDto newSettings) {
		super(time);
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
