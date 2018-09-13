package pl.konradmaksymilian.turnbasedgames.game.dto;

import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.dto.DontGetAngryGameResponseDtoFactory;

@Component
public class GameResponseDtoFactoryCreator {
	
	private DontGetAngryGameResponseDtoFactory dontGetAngryGameResponseDtoFactory;

	public GameResponseDtoFactoryCreator(DontGetAngryGameResponseDtoFactory dontGetAngryGameResponseDtoFactory) {
		this.dontGetAngryGameResponseDtoFactory = dontGetAngryGameResponseDtoFactory;
	}
	
	public GameResponseDtoFactory getFactory(Game game) {
		switch (game) {
			case DONT_GET_ANGRY:
				return dontGetAngryGameResponseDtoFactory;
			default:
				throw new GameResponseDtoFactoryException("An unrecognised game");
		}
	}
}
