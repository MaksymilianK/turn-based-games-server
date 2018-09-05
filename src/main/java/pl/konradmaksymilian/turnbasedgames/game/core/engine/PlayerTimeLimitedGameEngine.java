package pl.konradmaksymilian.turnbasedgames.game.core.engine;

import pl.konradmaksymilian.turnbasedgames.game.core.engine.player.PlayerManager;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.player.SimplePlayer;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.timer.PlayerTimeLimitedGameTimer;

public abstract class PlayerTimeLimitedGameEngine<T extends PlayerManager<? extends SimplePlayer>>
		extends StandardGameEngine<PlayerTimeLimitedGameTimer, T> {

	public PlayerTimeLimitedGameEngine(EventManager eventManager, PlayerTimeLimitedGameTimer timer, T playerManager) {
		super(eventManager, timer, playerManager);
	}
	
	@Override
	protected void initialize() {
		timer.setPlayersTimes(playerManager.getTeams());
	}
	
	@Override
	protected void onStart() {
		int team = playerManager.start();
		timer.nextTurn(team);
	}
	
	protected void nextTurn() {
		int team = playerManager.nextTeam();
		timer.nextTurn(team);
	}
}
