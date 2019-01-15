package pl.konradmaksymilian.turnbasedgames.game.core.business.engine;

import java.util.HashMap;

import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.event.GameEventManager;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.player.StandardPlayerManager;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.timer.PlayerTimeLimitedGameTimer;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameDetailsDto;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.PlayerTimeLimitedGameDetailsDto;

public abstract class PlayerTimeLimitedGameEngine<T1 extends StandardPlayerManager<?, ?>>
		extends StandardGameEngine<PlayerTimeLimitedGameTimer, T1> {

	public PlayerTimeLimitedGameEngine(GameEventManager eventManager, PlayerTimeLimitedGameTimer timer, T1 playerManager) {
		super(eventManager, timer, playerManager);
	}
	
	@Override
	public GameDetailsDto getGameDetails() {
		var playersTimes = new HashMap<Integer, Integer>();
		timer.getPlayersTimes().forEach((team, time) -> playersTimes.put(team, (int) time.toMillis()));
		
		return PlayerTimeLimitedGameDetailsDto.builder()
				.currentlyMovingTeam(playerManager.getCurrentlyMovingTeam())
				.gameStart(timer.getGameStart().toEpochMilli())
				.gameStatusDetails(getStatusDetails())
				.players(playerManager.getPlayers())
				.playersTimes(playersTimes)
				.settings(getSettings())
				.status(status)
				.build();
	}
	
	@Override
	protected void initialize() {
		timer.initialize(playerManager.getTeams());
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
