package pl.konradmaksymilian.turnbasedgames.user.event;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.user.entity.User;
import pl.konradmaksymilian.turnbasedgames.user.repository.LoggedUserManager;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

	private LoggedUserManager loggedUserManager;
	
	public AuthenticationSuccessEventListener(LoggedUserManager loggedUserManager) {
		this.loggedUserManager = loggedUserManager;
	}
	
	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		loggedUserManager.add((User) event.getAuthentication().getPrincipal());
	}
}
