package pl.konradmaksymilian.turnbasedgames.game.core.engine;

import java.util.Set;

import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.command.GameCommand;

public interface GameEngine {

	Game getGame();
	GameStatus getStatus();
	void addPlayer(int userId);
	int getMaxPlayers();
	int countPlayers();
	boolean containsPlayer(int userId);
	void removePlayer(int userId);
	Set<Integer> getPlayersIds();
	void processCommand(GameCommand command);
}
