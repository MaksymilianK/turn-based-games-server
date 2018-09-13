package pl.konradmaksymilian.turnbasedgames.game.core.action;

public enum CommonGameActionName {

	DICE_ROLL,
	TOKEN_MOVE,
	BUILDING,
	PASS;
	
	public int code() {
		return ordinal() + 50;
	}
}
