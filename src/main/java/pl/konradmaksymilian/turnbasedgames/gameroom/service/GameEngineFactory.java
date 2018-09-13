package pl.konradmaksymilian.turnbasedgames.gameroom.service;

import pl.konradmaksymilian.turnbasedgames.game.core.dto.GameSettingsDto;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameEngine;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.dto.DontGetAngryGameSettingsDto;

public abstract class GameEngineFactory<T extends GameSettingsDto> {
	
	protected RoomMessageSender messageSender;
	
	public GameEngineFactory(RoomMessageSender messageSender) {
		this.messageSender = messageSender;
	}
	
	public abstract GameEngine create(T settings);
}
