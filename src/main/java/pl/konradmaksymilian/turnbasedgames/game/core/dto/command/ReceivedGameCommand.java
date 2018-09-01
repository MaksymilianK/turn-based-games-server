package pl.konradmaksymilian.turnbasedgames.game.core.dto.command;

public abstract class ReceivedGameCommand implements GameCommand {

	private final int senderId;
	
	public ReceivedGameCommand(int senderId) {
		this.senderId = senderId;
	}
	
	public int getSenderId() {
		return senderId;
	}
}
