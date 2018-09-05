package pl.konradmaksymilian.turnbasedgames.game.core.engine.player;

public class SimplePlayer {

	private final int userId;
	
	private int team;
	
	public SimplePlayer(int userId, int team) {
		this.userId = userId;
		this.team = team;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setTeam(int team) {
		this.team = team;
	}
	
	public int getTeam() {
		return team;
	}
}
