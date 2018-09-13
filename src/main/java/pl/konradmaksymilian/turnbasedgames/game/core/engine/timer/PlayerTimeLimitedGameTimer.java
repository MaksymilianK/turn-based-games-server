package pl.konradmaksymilian.turnbasedgames.game.core.engine.timer;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class PlayerTimeLimitedGameTimer extends GameTimer {

	private final Map<Integer, Duration> playersTimes = new HashMap<>();
	
	private Runnable onPlayerTimeEnd;
	
	private Duration playerTime;
	
	private int currentlyMovingPlayer;
	
	public PlayerTimeLimitedGameTimer(Duration playerTime) {
		this.playerTime = playerTime;
	}
	
	protected void start(int player) {
		if (playersTimes.isEmpty() || onPlayerTimeEnd == null) {
			throw new GameTimerException("Game timer not initialised");
		}
		
		currentlyMovingPlayer = player;
		schedule(playerTime, onPlayerTimeEnd);
	}
	
	public void nextTurn(int player) {
		stop();
		var playerTimeLeft = playersTimes.get(currentlyMovingPlayer).minus(
				Duration.between(currentExecutionStart, getNow()));
		playersTimes.put(currentlyMovingPlayer, playerTimeLeft);
		
		schedule(playersTimes.get(player), onPlayerTimeEnd);
		currentlyMovingPlayer = player;
	}
	
	public Duration getPlayerTime(int player) {
		return playersTimes.get(player);
	}
	
	public void setPlayerTime(Duration playerTime) {
		this.playerTime = playerTime;
	}
	
	public void initialize(Set<Integer> players) {
		players.forEach(player -> playersTimes.put(player, playerTime));
	}
	
	public void removePlayer(int player) {
		playersTimes.remove(player);
	}
	
	public Map<Integer, Duration> getPlayersTimes() {
		return Collections.unmodifiableMap(playersTimes);
	}
	
	public Duration getPlayerTime() {
		return playerTime;
	}
}
