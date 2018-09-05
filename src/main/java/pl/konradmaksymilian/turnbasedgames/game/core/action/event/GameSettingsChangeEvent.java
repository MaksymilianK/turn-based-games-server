package pl.konradmaksymilian.turnbasedgames.game.core.action.event;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.GameSettingsDto;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.SharedGameAction;

public final class GameSettingsChangeEvent extends GameEvent {

	private final GameSettingsDto newSettings;
	
	public GameSettingsChangeEvent(GameSettingsDto newSettings) {
		this.newSettings = newSettings;
	}
	
	/**
 	* @param time the message sending time (millis from epoch)
 	*/
	public GameSettingsChangeEvent(long time, GameSettingsDto newSettings) {
		super(time);
		this.newSettings = newSettings;
	}
	
	public GameSettingsDto getNewSettings() {
		return newSettings;
	}
	
	@Override
	public int getCode() {
		return SharedGameAction.GAME_SETTINGS_CHANGE.code();
	}
}
