package pl.konradmaksymilian.turnbasedgames.game.dga.engine.player;

import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.player.StandardPlayerManager;

public final class DgaPlayerManager extends StandardPlayerManager<DgaPlayer, DgaPlayerFactory> {
	
	public DgaPlayerManager(DgaPlayerFactory playerFactory, int maxPlayers) {
		super(playerFactory, maxPlayers);
	}
}
