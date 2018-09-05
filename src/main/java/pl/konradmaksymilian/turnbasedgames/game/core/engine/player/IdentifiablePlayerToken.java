package pl.konradmaksymilian.turnbasedgames.game.core.engine.player;

public class IdentifiablePlayerToken extends SimplePlayerToken {

	private final int id;

	public IdentifiablePlayerToken(int team, int id) {
		super(team);
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
