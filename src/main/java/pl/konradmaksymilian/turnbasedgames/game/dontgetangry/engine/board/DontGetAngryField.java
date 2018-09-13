package pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine.board;

import pl.konradmaksymilian.turnbasedgames.game.core.board.IdentifiablePlayerToken;
import pl.konradmaksymilian.turnbasedgames.game.core.board.SingleTokenField;

public final class DontGetAngryField extends SingleTokenField<IdentifiablePlayerToken> {

	private final DontGetAngryFieldType type;
	
	public DontGetAngryField(int id, DontGetAngryFieldType type) {
		super(id);
		this.type = type;
	}
	
	public DontGetAngryFieldType getType() {
		return type;
	}
}
