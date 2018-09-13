package pl.konradmaksymilian.turnbasedgames.game.core.engine.player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;

public abstract class StandardPlayerManager<T1 extends SimplePlayer, T2 extends PlayerFactory<T1>> {

	protected int currentlyMovingTeam;
	
	protected final T2 playerFactory;
	
	protected final Map<Integer, T1> players = new HashMap<>();
		
	private int maxPlayers;
	
	public StandardPlayerManager(T2 playerFactory, int maxPlayers) {
		this.playerFactory = playerFactory;
		this.maxPlayers = maxPlayers;
	}
	
	public int getCurrentlyMovingTeam() {
		return currentlyMovingTeam;
	}
	
	public Map<Integer, Integer> getPlayers() {
		var players = new HashMap<Integer, Integer>();
		this.players.forEach((team, player) -> players.put(team, player.getUserId()));
		return Collections.unmodifiableMap(players);
	}
	
	public int start() {
		throwExceptionIfTooFewPlayers();
		
		currentlyMovingTeam = -1;
		return nextTeam();
	}
	
	public void start(int firstTeam) {
		throwExceptionIfTooFewPlayers();
		if (!players.containsKey(firstTeam)) {
			throw new PlayerManagerException("Team " + firstTeam + " cannot start because does not exist");
		}
		
		nextTeam(firstTeam);
	}
	
	public int nextTeam() {
		int i = currentlyMovingTeam + 1;
		while (!players.containsKey(i)) {
			if (i >= getMaxPlayers()) {
				i = 0;
			} else {
				i++;
			}
		}
		currentlyMovingTeam = i;
		return currentlyMovingTeam;
	}
	
	public void nextTeam(int team) {
		currentlyMovingTeam = team;
	}
	
	public Set<Integer> getTeams() {
		return players.keySet();
	}

	public void addPlayer(int userId) {
		if (players.size() == getMaxPlayers()) {
			throw new BadOperationException("Cannot add a player; all the teams are occupied by players");
		}
		
		int i = 0;
		while (players.containsKey(i)) {
			i++;
		}
		
		players.put(i, playerFactory.create(userId, i));
	}
	
	public void addPlayer(int userId, int team) {
		if (players.containsKey(team)) {
			throw new BadOperationException("Cannot add a player; the team: " + team + " is already occupied");
		}
	}

	public T1 getPlayer(int team) {
		if (players.containsKey(team)) {
			return players.get(team);
		} else {
			throw new PlayerManagerException("Cannot find a team: " + team);
		}
	}
	
	public void changeTeam(int senderId, int newTeam) {
		var currentTeam = findTeamByUserId(senderId);
		
		if (newTeam == currentTeam) {
			throw new BadOperationException("Cannot move a player to the same team");
		} else if (newTeam >= getMaxPlayers()) {
			throw new BadOperationException("The new team: " + newTeam + " does not exist");
		} else if (players.containsKey(newTeam)) {
			throw new BadOperationException("Cannot move a player to the new team; the new team is not free");
		}
		
		movePlayer(players.remove(currentTeam), newTeam);
	}

	public void shiftTeams(int firstTeam, int secondTeam) {
		if (!players.containsKey(firstTeam)) {
			throw new BadOperationException("There is no such a team: " + firstTeam);
		} else if (secondTeam >= getMaxPlayers()) {
			throw new BadOperationException("The new team: " + secondTeam + " does not exist");
		}
		
		var player = players.get(firstTeam);
		if (players.containsKey(secondTeam)) {
			movePlayer(players.get(secondTeam), firstTeam);
		}
		movePlayer(player, secondTeam);
	}

	public int countPlayers() {
		return players.size();
	}

	public boolean containsPlayerByUserId(int userId) {
		return players.values().stream()
				.anyMatch(player -> player.getUserId() == userId);
	}
	
	public int getCurrentlyMovingUserId() {
		return getCurrentlyMovingPlayer().getUserId();
	}
	
	public T1 getCurrentlyMovingPlayer() {
		return players.get(currentlyMovingTeam);
	}
	
	public int removePlayerByUserId(int userId) {
		for (int i = 0; i < getMaxPlayers(); i++) {
			if (players.containsKey(i)) {
				if (players.get(i).getUserId() == userId) {
					players.remove(i);
					return i;
				}
			}
		}
		throw new PlayerManagerException("Cannot delete a player with user id: " + userId + "; the player has not been "
				+ "found");
	}
	
	public int getMinPlayers() {
		return 2;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	protected void throwExceptionIfTooFewPlayers() {
		if (countPlayers() < getMinPlayers()) {
			throw new PlayerManagerException("Too few players in the game");
		}
	}
	
	private int findTeamByUserId(int userId) {
		return players.values().stream()
				.filter(player -> player.getUserId() == userId)
				.map(player -> player.getTeam())
				.findAny().orElseThrow(
						() -> new PlayerManagerException("Cannot find a team by the given user id: " + userId));
	}
	
	private void movePlayer(T1 player, int newTeam) {
		player.setTeam(newTeam);
		players.put(newTeam, player);
	}
}
