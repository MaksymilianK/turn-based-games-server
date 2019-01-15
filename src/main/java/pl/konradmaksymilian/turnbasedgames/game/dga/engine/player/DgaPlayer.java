package pl.konradmaksymilian.turnbasedgames.game.dga.engine.player;

import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.board.IdentifiablePlayerToken;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.player.PlayerWithMultipleTokens;

public final class DgaPlayer extends PlayerWithMultipleTokens<IdentifiablePlayerToken> {

	public DgaPlayer(String nick, int team) {
		super(nick, team);
	}
}
