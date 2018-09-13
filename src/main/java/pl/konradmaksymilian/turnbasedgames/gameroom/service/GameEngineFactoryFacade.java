package pl.konradmaksymilian.turnbasedgames.gameroom.service;

import java.util.Set;

import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameEngine;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameStatus;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.dto.DontGetAngryGameSettingsDto;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine.DontGetAngryGameEngineFactory;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.GameCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.GameSettingsDto;

@Component
public class GameEngineFactoryFacade {
	
	private DontGetAngryGameEngineFactory dontGetAngryFactory;
	
	public GameEngineFactoryFacade(DontGetAngryGameEngineFactory dontGetAngryFactory) {
		this.dontGetAngryFactory = dontGetAngryFactory;
	}

	public GameEngine create(GameSettingsDto settings) {
		switch (settings.getGame()) {
				case DONT_GET_ANGRY:
					return dontGetAngryFactory.create((DontGetAngryGameSettingsDto) settings);
				default:
					throw new GameEngineFactoryFacadeException("An unrecognised game: '" + settings.getGame() + "'");
		}
	}
}
