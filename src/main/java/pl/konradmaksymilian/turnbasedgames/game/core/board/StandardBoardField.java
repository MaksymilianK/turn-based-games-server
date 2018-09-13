package pl.konradmaksymilian.turnbasedgames.game.core.board;

public abstract class StandardBoardField {

	private final int id;

	public StandardBoardField(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
