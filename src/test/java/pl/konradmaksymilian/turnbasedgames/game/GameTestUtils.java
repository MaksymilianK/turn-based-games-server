package pl.konradmaksymilian.turnbasedgames.game;

import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.mockito.Mockito;

import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameEngine;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameStatus;

public class GameEngineTestUtils {
	
	private GameEngineTestUtils() {}
	
	public static GameEngine mockEngine() {
		return (new MockEngineBuilder()).build();
	}

	public static class MockEngineBuilder {
		
		private Set<Integer> playersIds = new HashSet<>();
		private Game game = Game.DONT_GET_ANGRY;
		private int maxPlayers = 4;
		private GameStatus gameStatus = GameStatus.NOT_STARTED;
		
		public MockEngineBuilder playersIds(Set<Integer> playersIds) {
			this.playersIds = playersIds;
			return this;
		}

		public MockEngineBuilder game(Game game) {
			this.game = game;
			return this;
		}

		public MockEngineBuilder maxPlayers(int maxPlayers) {
			this.maxPlayers = maxPlayers;
			return this;
		}
		
		public MockEngineBuilder gameStatus(GameStatus gameStatus) {
			this.gameStatus = gameStatus;
			return this;
		}
		
		public GameEngine build() {
			var engine = Mockito.mock(GameEngine.class);
			when(engine.getPlayersUsersIds()).thenReturn(playersIds);
			when(engine.getGame()).thenReturn(game);
			when(engine.getMaxPlayers()).thenReturn(maxPlayers);
			when(engine.getStatus()).thenReturn(gameStatus);
			when(engine.countPlayers()).thenReturn(playersIds.size());
			playersIds.forEach(playerId -> when(engine.containsPlayer(playerId)).thenReturn(true));
			
			return engine;
		}
	}
}
