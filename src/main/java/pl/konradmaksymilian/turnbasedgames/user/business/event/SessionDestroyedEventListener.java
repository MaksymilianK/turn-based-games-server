package pl.konradmaksymilian.turnbasedgames.user.business.event;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.user.data.entity.User;
import pl.konradmaksymilian.turnbasedgames.user.data.repository.LoggedUserManager;

@Component
public class SessionDestroyedEventListener implements ApplicationListener<HttpSessionDestroyedEvent> {

	private LoggedUserManager loggedUserManager;
	
	public SessionDestroyedEventListener(LoggedUserManager loggedUserManager) {
		this.loggedUserManager = loggedUserManager;
	}

	@Override
	public void onApplicationEvent(HttpSessionDestroyedEvent event) {
		event.getSecurityContexts().forEach(context -> loggedUserManager.remove(
				((User) context.getAuthentication().getPrincipal()).getId()));
		throw new RuntimeException("");
	}
}
