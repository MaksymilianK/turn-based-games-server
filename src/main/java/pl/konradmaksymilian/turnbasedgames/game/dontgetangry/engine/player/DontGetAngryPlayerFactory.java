package pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine.player;

import pl.konradmaksymilian.turnbasedgames.game.core.engine.player.PlayerFactory;

public final class DontGetAngryPlayerFactory implements PlayerFactory<DontGetAngryPlayer> {

	@Override
	public DontGetAngryPlayer create(int userId, int team) {
		return new DontGetAngryPlayer(userId, team);
	}
}
