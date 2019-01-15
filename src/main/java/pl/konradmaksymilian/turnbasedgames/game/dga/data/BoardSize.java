package pl.konradmaksymilian.turnbasedgames.game.dga.data;

public enum BoardSize {

	SMALL (40),
	MEDIUM (48),
	BIG (56),
	LARGE (64),
	HUGE (72);
	
	private final int fields;
	
	BoardSize(int fields) {
		this.fields = fields;
	}
	
	public int fields() {
		return fields;
	}
	
	public static BoardSize ofFields(int fields) {
		BoardSize boardSize = null;
		for (BoardSize size : BoardSize.values()) {
			if (fields == size.fields()) {
				boardSize = size;
			}
		}
		
		if (boardSize == null) {
			throw new IllegalArgumentException("Unapropriate number of fields " + fields);
		} else {
			return boardSize;
		}
	}
}
