package pl.konradmaksymilian.turnbasedgames.game.core.data.event;

public enum CommonGameEventName {

	DICE_ROLL,
	TOKEN_MOVE,
	BUILDING,
	PASS;
	
	public int code() {
		return ordinal() + 50;
	}
}
