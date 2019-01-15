package pl.konradmaksymilian.turnbasedgames.game.dga.engine;

import java.time.Duration;

import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.GameEngine;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.StandardDice;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.event.GameEventManager;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.timer.PlayerTimeLimitedGameTimer;
import pl.konradmaksymilian.turnbasedgames.game.core.business.factory.GameEngineFactory;
import pl.konradmaksymilian.turnbasedgames.game.dga.engine.board.DgaBoard;
import pl.konradmaksymilian.turnbasedgames.game.dga.engine.player.DgaPlayerFactory;
import pl.konradmaksymilian.turnbasedgames.game.dga.engine.player.DgaPlayerManager;
import pl.konradmaksymilian.turnbasedgames.game.dga.web.dto.DgaSettingsRequestDto;

@Component
public class DgaEngineFactory extends GameEngineFactory<DgaPlayerFactory, DgaSettingsRequestDto> {
	
	private StandardDice dice;
	
	public DgaEngineFactory(DgaPlayerFactory playerFactory, StandardDice dice) {
		super(playerFactory);
		this.dice = dice;
	}

	@Override
	public GameEngine create(GameEventManager eventManager, DgaSettingsRequestDto settings) {
		return new DgaEngine(eventManager, new PlayerTimeLimitedGameTimer(Duration.ofSeconds(settings.getPlayerTime())),
				new DgaPlayerManager(playerFactory, settings.getMaxPlayers()),
				new DgaBoard(settings.getBoardSize().fields()), dice);
	}
}
