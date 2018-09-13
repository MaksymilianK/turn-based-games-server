package pl.konradmaksymilian.turnbasedgames.game.dto;

import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameEngine;

public interface GameResponseDtoFactory<T extends GameEngine> {

	GameResponseDto create(T gameEngine);
}
