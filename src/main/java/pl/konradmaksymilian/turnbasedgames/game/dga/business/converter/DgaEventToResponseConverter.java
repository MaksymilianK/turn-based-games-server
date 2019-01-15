package pl.konradmaksymilian.turnbasedgames.game.dga.business.converter;

import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.game.core.business.converter.GameEventToResponseConverter;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.CommonGameEventName;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.DiceRollEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.GameEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.IdentifiableTokenMoveEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response.DiceRollResponse;
import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.response.IdentifiableTokenMoveResponse;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomResponse;

@Component
public class DgaEventToResponseConverter extends GameEventToResponseConverter {

	@Override
	protected RoomResponse convertSpecific(GameEvent event) {
		var time = event.getTime().toEpochMilli();
		if (event.getCode() == CommonGameEventName.DICE_ROLL.code()) {
			return new DiceRollResponse(((DiceRollEvent) event).getRolledValue(), time);
		} else if (event.getCode() == CommonGameEventName.TOKEN_MOVE.code()) {
			return new IdentifiableTokenMoveResponse(((IdentifiableTokenMoveEvent) event).getToken(), time);
		} else {
			return null;
		}
	}
}
