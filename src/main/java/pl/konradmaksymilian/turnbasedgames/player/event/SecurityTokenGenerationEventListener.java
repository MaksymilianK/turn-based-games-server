package pl.konradmaksymilian.turnbasedgames.player.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.core.service.MailingService;

@Component
public class SecurityTokenGenerationEventListener implements ApplicationListener<SecurityTokenGenerationEvent> {

	private MailingService mailingService;
	
	public SecurityTokenGenerationEventListener(MailingService mailingService) {
		this.mailingService = mailingService;
	}

	@Override
	public void onApplicationEvent(SecurityTokenGenerationEvent event) {
		// todo
		mailingService.prepare()
			.from("")
			.to(event.getEmail())
			.subject("Security token")
			.text("Player's id: " + event.getPlayerId() + ", security token: " + event.getToken())
			.send();
	}
}
