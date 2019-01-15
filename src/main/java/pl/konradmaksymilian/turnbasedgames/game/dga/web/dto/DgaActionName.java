package pl.konradmaksymilian.turnbasedgames.game.dga.web.dto;

public enum DgaActionName {

	DICE_ROLL,
	MOVE;
	
	public int code() {
		return 100 + ordinal();
	}
}
