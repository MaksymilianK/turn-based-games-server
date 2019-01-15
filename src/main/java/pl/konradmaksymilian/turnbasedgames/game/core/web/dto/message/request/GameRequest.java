package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request;

import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.request.RoomRequest;
import pl.konradmaksymilian.turnbasedgames.user.data.entity.User;

public abstract class GameRequest implements RoomRequest {
	
	private User sender;
	
	public User getSender() {
		if (sender == null) {
			throw new GameMessageException("The sender has not been set");
		} else {
			return sender;
		}
	}
	
	public void setSender(User sender) {
		if (this.sender == null) {
			this.sender = sender;
		} else {
			throw new GameMessageException("The sender has been already set");
		}
	}
}
