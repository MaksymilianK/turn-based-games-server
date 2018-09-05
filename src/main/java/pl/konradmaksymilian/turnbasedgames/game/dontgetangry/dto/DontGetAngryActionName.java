package pl.konradmaksymilian.turnbasedgames.game.dontgetangry.dto;

public enum DontGetAngryActionName {

	DICE_ROLL,
	MOVE;
	
	public int code() {
		return 100 + ordinal();
	}
}
