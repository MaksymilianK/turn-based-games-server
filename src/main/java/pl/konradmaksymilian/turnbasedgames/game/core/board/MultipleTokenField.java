package pl.konradmaksymilian.turnbasedgames.game.core.board;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MultipleTokenField<T extends SimplePlayerToken> extends StandardBoardField {
	
	private final Set<T> tokens = new HashSet<>();
	
	public MultipleTokenField(int id) {
		super(id);
	}
	
	public boolean containsToken() {
		return !tokens.isEmpty();
	}
	
	public boolean containsTokenOf(int team) {
		return tokens.stream()
				.anyMatch(token -> token.getTeam() == team);
	}
	
	public Set<T> getTokens() {
		return Collections.unmodifiableSet(tokens);
	}
	
	public void addToken(T token) {
		if (!tokens.add(token)) {
			throw new BoardException("Cannot add the token of team " + token.getTeam() + " to a field; the token is "
					+ "already present on this field");
		}
	}
	
	public T removeToken(int team) {
		var tokenToRemove = tokens.stream()
				.filter(token -> token.getTeam() == team)
				.findAny().orElseThrow(() -> new BoardException("Cannot remove the token with of team " + team 
						+ " from a field; the token is not on this field"));
		tokens.remove(tokenToRemove);
		return tokenToRemove;
	}
}
