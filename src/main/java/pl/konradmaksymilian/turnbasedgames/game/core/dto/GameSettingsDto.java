package pl.konradmaksymilian.turnbasedgames.game.core.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import pl.konradmaksymilian.turnbasedgames.game.Game;
import pl.konradmaksymilian.turnbasedgames.game.dontgetangry.dto.DontGetAngryGameSettingsDto;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "game")
@JsonSubTypes({
	@JsonSubTypes.Type(value = DontGetAngryGameSettingsDto.class, name = "DONT_GET_ANGRY")
})
public interface GameSettingsDto {
	
	Game getGame();
}
