package pl.konradmaksymilian.turnbasedgames.game.core.engine.player;

import java.util.Set;

public interface PlayerManager<T extends SimplePlayer> {

	int start();
	void start(int team);
	int nextTeam();
	void nextTeam(int team);
	Set<Integer> getTeams();
	void addPlayer(int userId);
	void addPlayer(int userId, int team);
	T getPlayer(int team);
	void changeTeam(int senderId, int newTeam);
	void shiftTeams(int firstTeam, int secondTeam);
	int getMinPlayers();
	int getMaxPlayers();
	int countPlayers();
	int getCurrentlyMovingTeam();
	int getCurrentlyMovingUserId();
	T getCurrentlyMovingPlayer();
	boolean containsPlayerByUserId(int userId);
	void removePlayerByUserId(int userId);
	Set<Integer> getPlayersUsersIds();
}
