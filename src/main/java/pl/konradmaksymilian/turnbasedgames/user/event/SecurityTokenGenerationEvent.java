package pl.konradmaksymilian.turnbasedgames.user.event;

import org.springframework.context.ApplicationEvent;

public class SecurityTokenGenerationEvent extends ApplicationEvent {

	private final String token;
	
	private final int userId;
	
	private final String email;
	
	public SecurityTokenGenerationEvent(Object source, String token, Integer userId, String email) {
		super(source);
		this.token = token;
		this.userId = userId;
		this.email = email;
	}
	
	public String getToken() {
		return token;
	}
	
	public int getPlayerId() {
		return userId;
	}
	
	public String getEmail() {
		return email;
	}
}
