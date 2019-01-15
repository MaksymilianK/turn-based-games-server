package pl.konradmaksymilian.turnbasedgames.game.core.web.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import pl.konradmaksymilian.turnbasedgames.game.core.business.Gameable;
import pl.konradmaksymilian.turnbasedgames.game.dga.web.dto.DgaSettingsRequestDto;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "game")
@JsonSubTypes({
	@JsonSubTypes.Type(value = DgaSettingsRequestDto.class, name = "DONT_GET_ANGRY")
})
public interface GameSettingsRequestDto extends Gameable {}