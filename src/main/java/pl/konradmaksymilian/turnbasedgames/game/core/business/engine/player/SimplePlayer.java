package pl.konradmaksymilian.turnbasedgames.game.core.business.engine.player;

public class SimplePlayer {

	private final String userNick;
	
	private int team;
	
	public SimplePlayer(String userNick, int team) {
		this.userNick = userNick;
		this.team = team;
	}
	
	public String getUserNick() {
		return userNick;
	}
	
	public void setTeam(int team) {
		this.team = team;
	}
	
	public int getTeam() {
		return team;
	}
}
