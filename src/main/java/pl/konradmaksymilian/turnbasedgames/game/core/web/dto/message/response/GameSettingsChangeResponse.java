package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response;

import pl.konradmaksymilian.turnbasedgames.game.core.data.event.SharedGameEventName;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsChangeResponseDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomResponse;

public final class GameSettingsChangeResponse extends RoomResponse {

	private final GameSettingsChangeResponseDto newSettings;
	
	/**
 	* @param time the message sending time (millis from epoch)
 	*/
	public GameSettingsChangeResponse(GameSettingsChangeResponseDto newSettings, long time) {
		super(time);
		this.newSettings = newSettings;
	}
	
	public GameSettingsChangeResponseDto getNewSettings() {
		return newSettings;
	}
	
	@Override
	public int getCode() {
		return SharedGameEventName.GAME_SETTINGS_CHANGE.code();
	}
}
