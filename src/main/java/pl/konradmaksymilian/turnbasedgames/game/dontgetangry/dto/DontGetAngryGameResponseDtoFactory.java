package pl.konradmaksymilian.turnbasedgames.game.dontgetangry.dto;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.engine.DontGetAngryGameEngine;
import pl.konradmaksymilian.turnbasedgames.game.dto.GameResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.dto.GameResponseDtoFactory;

@Component
public class DontGetAngryGameResponseDtoFactory implements GameResponseDtoFactory<DontGetAngryGameEngine> {

	@Override
	public GameResponseDto create(DontGetAngryGameEngine gameEngine) {
		var dtoBuilder = (new DontGetAngryGameResponseDto.Builder())
				.playersIds(gameEngine.getPlayerManager().getPlayers())
				.status(gameEngine.getStatus());
		
		if (!gameEngine.isNotStarted()) {
			var playersTimes = new HashMap<Integer, Integer>();
			gameEngine.getTimer().getPlayersTimes().forEach((team, time) -> playersTimes.put(team, (int) time.getSeconds()));
			
			dtoBuilder
					.currentlyMovingTeam(gameEngine.getPlayerManager().getCurrentlyMovingTeam())
					.gameStart(gameEngine.getTimer().getGameStart().toEpochMilli())
					.lastDiceRolls(gameEngine.countLastDiceRolls())
					.playersTimes(playersTimes)
					.playerTime((int) gameEngine.getTimer().getPlayerTime().getSeconds())
					.tokens(gameEngine.getBoard().getTokensPositions());
		}
		
		return dtoBuilder.create();
	}
}
