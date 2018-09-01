package pl.konradmaksymilian.turnbasedgames.game.core.dto.command;

public abstract class PublishableUserGameCommand extends PublishableGameCommand {

	private final int senderId;
	
	public PublishableUserGameCommand(long time, int senderId) {
		super(time);
		this.senderId = senderId;
	}
	
	public PublishableUserGameCommand(int senderId) {
		this.senderId = senderId;
	}
	
	public int getSenderId() {
		return senderId;
	}
}
