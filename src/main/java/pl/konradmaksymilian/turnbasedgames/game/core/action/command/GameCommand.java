package pl.konradmaksymilian.turnbasedgames.game.core.action.command;

import pl.konradmaksymilian.turnbasedgames.game.core.action.GameAction;

public abstract class GameCommand implements GameAction {

	private Integer senderId;
	
	public int getSenderId() {
		if (senderId == null) {
			throw new GameCommandException("A sender's ID has not been set");
		} else {
			return senderId;
		}
	}
	
	public void setSenderId(int senderId) {
		if (this.senderId == null) {
			this.senderId = senderId;
		} else {
			throw new GameCommandException("A sender's ID has been already set");
		}
	}
	
	public abstract boolean isHostCommand();
}
