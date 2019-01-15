package pl.konradmaksymilian.turnbasedgames.user.business.event;

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
		// not implemented yet
	}
}
