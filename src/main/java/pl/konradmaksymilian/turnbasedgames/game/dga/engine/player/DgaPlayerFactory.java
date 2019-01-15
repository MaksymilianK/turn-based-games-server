package pl.konradmaksymilian.turnbasedgames.game.dga.engine.player;

import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.player.PlayerFactory;

@Component
public final class DgaPlayerFactory implements PlayerFactory<DgaPlayer> {

	@Override
	public DgaPlayer create(String nick, int team) {
		return new DgaPlayer(nick, team);
	}
}
