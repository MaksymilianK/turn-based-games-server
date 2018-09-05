package pl.konradmaksymilian.turnbasedgames.gameroom.dto.message;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.GameSettingsDto;

public final class PublishableGameChangeMessage extends PublishableRoomMessage {

	private GameSettingsDto newSettings;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public PublishableGameChangeMessage(long time, GameSettingsDto newSettings) {
		super(time);
		this.newSettings = newSettings;
	}
	
	public PublishableGameChangeMessage(GameSettingsDto newSettings) {
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
