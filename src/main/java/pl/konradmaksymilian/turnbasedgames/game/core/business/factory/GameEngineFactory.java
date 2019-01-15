package pl.konradmaksymilian.turnbasedgames.game.core.business.factory;

import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.GameEngine;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.event.GameEventManager;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.player.PlayerFactory;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsRequestDto;

public abstract class GameEngineFactory<T1 extends PlayerFactory<?>, T2 extends GameSettingsRequestDto> {

	protected T1 playerFactory;
	
	public GameEngineFactory(T1 playerFactory) {
		this.playerFactory = playerFactory;
	}
	
	public abstract GameEngine create(GameEventManager eventManager, T2 settings);
}
