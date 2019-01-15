package pl.konradmaksymilian.turnbasedgames.game.core.business.converter;

import pl.konradmaksymilian.turnbasedgames.core.converter.ModelToDtoConverter;
import pl.konradmaksymilian.turnbasedgames.core.exception.UnexpectedCaseException;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.GameEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.SharedGameEventName;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response.GameCountdownStartResponse;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response.GameFinishResponse;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response.GameStartResponse;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomResponse;

public abstract class GameEventToResponseConverter implements ModelToDtoConverter<GameEvent, RoomResponse> {

	@Override
	public RoomResponse convert(GameEvent event) {
		var response = convertSpecific(event);
		if (response == null) {
			if (event.getCode() == SharedGameEventName.GAME_START.code()) {
				return new GameStartResponse(event.getTime().toEpochMilli());
			} else {
				throw new UnexpectedCaseException("Cannot convert event with code " + event.getCode());
			}
		}
		return response;
	}
	
	protected abstract RoomResponse convertSpecific(GameEvent event);
}
