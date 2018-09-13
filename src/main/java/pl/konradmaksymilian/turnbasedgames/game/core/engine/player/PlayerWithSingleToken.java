package pl.konradmaksymilian.turnbasedgames.game.core.engine.player;

import pl.konradmaksymilian.turnbasedgames.game.core.board.SimplePlayerToken;

public class PlayerWithSingleToken<T extends SimplePlayerToken> extends SimplePlayer {

	private T token = null;
	
	public PlayerWithSingleToken(int userId, int team) {
		super(userId, team);
	}
	
	public void setToken(T token) {
		this.token = token;
	}

	public T getToken() {
		return token;
	}
}
