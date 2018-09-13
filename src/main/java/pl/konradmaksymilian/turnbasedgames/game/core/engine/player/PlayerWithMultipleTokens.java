package pl.konradmaksymilian.turnbasedgames.game.core.engine.player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pl.konradmaksymilian.turnbasedgames.game.core.board.IdentifiablePlayerToken;

public class PlayerWithMultipleTokens<T extends IdentifiablePlayerToken> extends SimplePlayer {

	private final Map<Integer, T> tokens = new HashMap<>();
	
	public PlayerWithMultipleTokens(int userId, int team) {
		super(userId, team);
	}
	
	public void setTokens(Collection<T> tokens) {
		tokens.clear();
		tokens.forEach(token -> this.tokens.put(token.getId(), token));
	}

	public T getToken(int id) {
		if (tokens.containsKey(id)) {
			return tokens.get(id);
		} else {
			throw new PlayerException("The player has not got a token with id: " + id);
		}
	}
}
