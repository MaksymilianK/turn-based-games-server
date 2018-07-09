package pl.konradmaksymilian.turnbasedgames;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TurnBasedGamesApp {
	
	private static final Logger logger = LoggerFactory.getLogger(TurnBasedGamesApp.class);
	
	public static void main(String[] args) {
		logger.info("Starting turn based games server!");
		SpringApplication.run(TurnBasedGamesApp.class);
	}
}
