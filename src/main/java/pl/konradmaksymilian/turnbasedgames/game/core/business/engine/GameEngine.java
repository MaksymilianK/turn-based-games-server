package pl.konradmaksymilian.turnbasedgames.game.core.business.engine;

import java.util.Collection;

import pl.konradmaksymilian.turnbasedgames.game.core.business.Gameable;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameDetailsDto;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameSettingsResponseDto;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request.GameRequest;
import pl.konradmaksymilian.turnbasedgames.room.business.RoomMessageSender;

public interface GameEngine extends Gameable {
	
	GameStatus getStatus();
	GameDetailsDto getGameDetails();
	GameSettingsResponseDto getSettings();
	Collection<String> getPlayers();
	int getMaxPlayers();
	void addPlayer(String nick);
	void removePlayer(String nick);
	void injectMessageSender(RoomMessageSender messageSender);
	void processMessage(GameRequest message);
}
