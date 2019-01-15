package pl.konradmaksymilian.turnbasedgames.game.core.data.event;

public enum SharedGameEventName {

	GAME_COUNTDOWN_START,
	GAME_START,
	GAME_SETTINGS_CHANGE,
	PLAYER_TEAM_CHANGE,
	PLAYER_TEAM_SHIFT,
	PLAYER_ESCAPE,
	GAME_HISTORY,
	GAME_FINISH;
	
	public int code() {
		return ordinal() + 100;
	}
}
