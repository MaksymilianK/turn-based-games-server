package pl.konradmaksymilian.turnbasedgames.game.core.engine.player;

import pl.konradmaksymilian.turnbasedgames.game.core.engine.ChessTeam;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.PlayerManagerException;

public abstract class ChessBasedPlayerManager<
				T1 extends PlayerWithMultipleTokens<? extends IdentifiablePlayerToken>,
				T2 extends PlayerFactory<T1>>
		extends StandardPlayerManager<T1, T2> {
	
	public ChessBasedPlayerManager(T2 playerFactory) {
		super(playerFactory, 2);
	}
	
	@Override
	public int start() {
		throwExceptionIfTooFewPlayers();
		nextTeam(0);
		return 0;
	}
	
	@Override
	public int nextTeam() {
		if (currentlyMovingTeam == 0) {
			currentlyMovingTeam = 1;
		} else {
			currentlyMovingTeam = 0;
		}
		return currentlyMovingTeam;
	}
	
	@Override
	public final int getMinPlayers() {
		return 2;
	}

	@Override
	public final int getMaxPlayers() {
		return 2;
	}
	
	public T1 getPlayer(ChessTeam team) {
		if (players.containsKey(team.ordinal())) {
			return players.get(team.ordinal());
		} else {
			throw new PlayerManagerException("");
		}
	}
}
