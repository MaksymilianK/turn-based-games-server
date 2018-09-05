package pl.konradmaksymilian.turnbasedgames.gameroom.service;

import java.util.Set;

import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameEngine;
import pl.konradmaksymilian.turnbasedgames.game.core.engine.GameStatus;
import pl.konradmaksymilian.turnbasedgames.game.core.action.command.GameCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.GameSettingsDto;

@Component
public class GameEngineFactory {
		
	public GameEngine create(GameSettingsDto settings) {
		switch (settings.getGame()) {
			case DONT_GET_ANGRY:
				return new GameEngine() {

					@Override
					public Game getGame() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public GameStatus getStatus() {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public void addPlayer(int userId) {
						// TODO Auto-generated method stub
					}

					@Override
					public int getMaxPlayers() {
						// TODO Auto-generated method stub
						return 0;
					}

					@Override
					public int countPlayers() {
						// TODO Auto-generated method stub
						return 0;
					}

					@Override
					public boolean containsPlayer(int userId) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public void removePlayer(int userId) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public Set<Integer> getPlayersUsersIds() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public void processCommand(GameCommand command) {
						// TODO Auto-generated method stub
					}
					
				}; //not implemented yet
			default:
				throw new GameEngineFactoryException("Unrecognised game");
		}
	}
}
