package pl.konradmaksymilian.turnbasedgames.game.core.engine;

import java.util.Set;

import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.GameCommand;
import pl.konradmaksymilian.turnbasedgames.gameroom.GameRoom;

public interface GameEngine {

	Game getGame();
	GameStatus getStatus();
	void addPlayer(int userId);
	int getMaxPlayers();
	int countPlayers();
	boolean containsPlayer(int userId);
	void removePlayer(int userId);
	void processCommand(GameCommand command);
	void injectRoomId(int roomId);
	Set<Integer> getPlayersUsersIds();
}
