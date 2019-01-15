package pl.konradmaksymilian.turnbasedgames.game.core.business.engine.player;

import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.board.SimplePlayerToken;

public class PlayerWithSingleToken<T extends SimplePlayerToken> extends SimplePlayer {

	private T token = null;
	
	public PlayerWithSingleToken(String userNick, int team) {
		super(userNick, team);
	}
	
	public void setToken(T token) {
		this.token = token;
	}

	public T getToken() {
		return token;
	}
}
