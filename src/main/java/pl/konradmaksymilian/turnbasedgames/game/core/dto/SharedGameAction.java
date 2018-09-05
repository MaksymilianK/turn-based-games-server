package pl.konradmaksymilian.turnbasedgames.game.core.dto;

public enum SharedGameAction {

	GAME_COUNTDOWN_START,
	GAME_START,
	GAME_SETTINGS_CHANGE,
	PLAYER_TEAM_CHANGE,
	PLAYER_TEAM_SHIFT;
	
	public int code() {
		return ordinal();
	}
}
