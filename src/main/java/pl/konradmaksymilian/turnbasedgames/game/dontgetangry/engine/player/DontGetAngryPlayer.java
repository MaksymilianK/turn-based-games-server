package pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine.player;

import pl.konradmaksymilian.turnbasedgames.game.core.engine.player.IdentifiablePlayerToken;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.player.PlayerWithMultipleTokens;

public final class DontGetAngryPlayer extends PlayerWithMultipleTokens<IdentifiablePlayerToken> {

	public DontGetAngryPlayer(int userId, int team) {
		super(userId, team);
	}
}
