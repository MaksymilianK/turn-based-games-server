package pl.konradmaksymilian.turnbasedgames.game.core.business.factory;

import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.game.core.business.converter.GameEventToResponseConverter;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.GameEngine;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.event.GameEventManager;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsRequestDto;
import pl.konradmaksymilian.turnbasedgames.game.dga.engine.DgaEngineFactory;
import pl.konradmaksymilian.turnbasedgames.game.dga.web.dto.DgaSettingsRequestDto;
import pl.konradmaksymilian.turnbasedgames.room.business.RoomMessageSender;

@Component
public class GameEngineFactoryFacade {
	
	private RoomMessageSender messageSender;
	private GameEventToResponseConverter eventConverter;
	private DgaEngineFactory dgaEngineFactory;
	
	public GameEngineFactoryFacade(RoomMessageSender messageSender, DgaEngineFactory dgaEngineFactory) {
		this.messageSender = messageSender;
		this.dgaEngineFactory = dgaEngineFactory;
	}

	public GameEngine create(GameSettingsRequestDto settings) {
		switch (settings.getGame()) {
				case DONT_GET_ANGRY:
					return dgaEngineFactory.create(createEventManager(), (DgaSettingsRequestDto) settings);
				default:
					throw new GameEngineFactoryFacadeException("An unrecognised game: '" + settings.getGame() + "'");
		}
	}
	
	private GameEventManager createEventManager() {
		return new GameEventManager(messageSender, eventConverter);
	}
}
