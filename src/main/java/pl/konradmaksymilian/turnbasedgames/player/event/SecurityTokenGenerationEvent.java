package pl.konradmaksymilian.turnbasedgames.player.event;

import org.springframework.context.ApplicationEvent;

public class SecurityTokenGenerationEvent extends ApplicationEvent {

	private final String token;
	
	private final int playerId;
	
	private final String email;
	
	public SecurityTokenGenerationEvent(Object source, String token, Integer playerId, String email) {
		super(source);
		this.token = token;
		this.playerId = playerId;
		this.email = email;
	}
	
	public String getToken() {
		return token;
	}
	
	public int getPlayerId() {
		return playerId;
	}
	
	public String getEmail() {
		return email;
	}
}
