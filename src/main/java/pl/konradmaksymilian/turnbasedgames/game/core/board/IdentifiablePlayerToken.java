package pl.konradmaksymilian.turnbasedgames.game.core.board;

public class IdentifiablePlayerToken extends SimplePlayerToken {

	private final int id;

	public IdentifiablePlayerToken(int team, int fieldId, int id) {
		super(team, fieldId);
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
