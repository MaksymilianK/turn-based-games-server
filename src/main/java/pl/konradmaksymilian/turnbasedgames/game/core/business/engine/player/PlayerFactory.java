package pl.konradmaksymilian.turnbasedgames.game.core.business.engine.player;

public interface PlayerFactory<T extends SimplePlayer> {

	public T create(String nick, int team);
}
