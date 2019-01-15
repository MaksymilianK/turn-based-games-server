package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response;

import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsResponseDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;

public final class GameChangeResponse extends RoomResponse {

	private GameSettingsResponseDto newSettings;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public GameChangeResponse(long time, GameSettingsResponseDto newSettings) {
		super(time);
		this.newSettings = newSettings;
	}
	
	public GameChangeResponse(GameSettingsResponseDto newSettings) {
		this.newSettings = newSettings;
	}
	
	public GameSettingsResponseDto getNewSettings() {
		return newSettings;
	}

	@Override
	public int getCode() {
		return SharedRoomMessageName.GAME_CHANGE.code();
	}
}
