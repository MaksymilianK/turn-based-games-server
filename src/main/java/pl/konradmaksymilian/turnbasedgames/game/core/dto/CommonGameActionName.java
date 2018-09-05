package pl.konradmaksymilian.turnbasedgames.game.core.dto;

public enum CommonGameActionName {

	DICE_ROLL,
	MOVE,
	BUILDING,
	PASS;
	
	public int code() {
		return ordinal() + 50;
	}
}
