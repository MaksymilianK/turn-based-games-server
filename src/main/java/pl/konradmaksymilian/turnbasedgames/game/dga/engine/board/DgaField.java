package pl.konradmaksymilian.turnbasedgames.game.dga.engine.board;

import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.board.IdentifiablePlayerToken;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.board.SingleTokenField;

public final class DgaField extends SingleTokenField<IdentifiablePlayerToken> {

	private final DgaFieldType type;
	
	public DgaField(int id, DgaFieldType type) {
		super(id);
		this.type = type;
	}
	
	public DgaFieldType getType() {
		return type;
	}
}
