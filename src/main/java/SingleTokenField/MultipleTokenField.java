package SingleTokenField;

import java.util.HashSet;
import java.util.Set;

public class MultipleTokenField<T extends SimplePlayerToken> extends BoardField {
	
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
	
	public void addToken(T token) {
		if (!tokens.add(token)) {
			throw new BoardException("Cannot add the token of team " + token.getTeam() + " to a field; the token is "
					+ "already present on this field");
		}
	}
	
	public void removeToken(int team) {
		if (!tokens.removeIf(token -> token.getTeam() == team)) {
			throw new BoardException("Cannot remove the token with of team " + team + " from a field; the token is not "
					+ "on this field");
		}
	}
}
