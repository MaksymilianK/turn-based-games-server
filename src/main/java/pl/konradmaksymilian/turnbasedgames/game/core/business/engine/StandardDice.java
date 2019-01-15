package pl.konradmaksymilian.turnbasedgames.game.core.business.engine;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class StandardDice {

	private final Random random = new Random();
	
	public int roll() {
		return random.nextInt(6) + 1;
	}
}
