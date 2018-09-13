package pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine.player;

import pl.konradmaksymilian.turnbasedgames.game.core.engine.player.StandardPlayerManager;

public final class DontGetAngryPlayerManager extends StandardPlayerManager<DontGetAngryPlayer, DontGetAngryPlayerFactory> {
	
	public DontGetAngryPlayerManager(DontGetAngryPlayerFactory playerFactory, int maxPlayers) {
		super(playerFactory, maxPlayers);
	}
}
