package SingleTokenField;

public abstract class BoardField {

	private final int id;

	public BoardField(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
