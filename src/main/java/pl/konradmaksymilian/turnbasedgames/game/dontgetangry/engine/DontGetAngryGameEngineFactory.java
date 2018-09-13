package pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine;

import java.time.Duration;

import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.game.core.engine.EventManager;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameEngine;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.timer.PlayerTimeLimitedGameTimer;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.utils.StandardDice;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.dto.DontGetAngryGameSettingsDto;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine.board.DontGetAngryBoard;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine.player.DontGetAngryPlayerFactory;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine.player.DontGetAngryPlayerManager;
import pl.konradmaksymilian.turnbasedgames.gameroom.service.GameEngineFactory;
import pl.konradmaksymilian.turnbasedgames.gameroom.service.RoomMessageSender;

@Component
public class DontGetAngryGameEngineFactory extends GameEngineFactory<DontGetAngryGameSettingsDto> {
	
	private DontGetAngryPlayerFactory playerFactory;
	
	private StandardDice dice;
	
	public DontGetAngryGameEngineFactory(RoomMessageSender messageSender, StandardDice dice) {
		super(messageSender);
		this.dice = dice;
	}

	@Override
	public GameEngine create(DontGetAngryGameSettingsDto settings) {
		return new DontGetAngryGameEngine(new EventManager(messageSender), 
				new PlayerTimeLimitedGameTimer(Duration.ofSeconds(settings.getPlayerTime())),
				new DontGetAngryPlayerManager(playerFactory, settings.getMaxPlayers()),
				new DontGetAngryBoard(settings.getBoardSize()), dice);
	}
}
