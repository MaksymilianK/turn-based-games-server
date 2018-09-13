package pl.konradmaksymilian.turnbasedgames.game.core.board;

import java.util.Optional;

public class SingleTokenField<T extends SimplePlayerToken> extends StandardBoardField {
	
	private T token = null;
	
	public SingleTokenField(int id) {
		super(id);
	}
	
	public boolean containsToken() {
		return token != null;
	}
	
	public boolean containsTokenOf(int team) {
		if (containsToken()) {
			return token.getTeam() == team;
		} else {
			return false;
		}
	}
	
	public Optional<T> getToken() {
		return Optional.ofNullable(token);
	}
	
	public void addToken(T token) {
		if (containsToken()) {
			throw new BoardException("Cannot add the token to a field; another token is already present on this field");
		} else {
			this.token = token;
		}
	}
	
	public T removeToken() {
		if (containsToken()) {
			var token = this.token;
			this.token = null;
			return token;
		} else {
			throw new BoardException("Cannot remove the token from a field because it is not present on this field");
		}
	}
}
