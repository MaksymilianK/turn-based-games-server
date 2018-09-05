package pl.konradmaksymilian.turnbasedgames.game.core.engine.player;

public interface PlayerFactory<T extends SimplePlayer> {

	public T create(int userId, int team);
}
