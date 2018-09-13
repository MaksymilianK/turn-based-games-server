package pl.konradmaksymilian.turnbasedgames.game.core.board;

public class ChessboardField<T extends SimplePlayerToken> extends SingleTokenField<T> {

	private final int x;
	private final int y;
	
	public ChessboardField(int x, int y) {
		super((y * 8) + x);
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
