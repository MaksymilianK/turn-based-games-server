package pl.konradmaksymilian.turnbasedgames.user.business.service;

import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import pl.konradmaksymilian.turnbasedgames.user.business.event.SecurityTokenGenerationEvent;
import pl.konradmaksymilian.turnbasedgames.user.data.repository.SecurityTokenCache;

@Service
public class SecurityTokenProvider {

	private final Logger logger = LoggerFactory.getLogger(SecurityTokenProvider.class);
	
	private SecurityTokenCache securityTokenCache;
		
	private ApplicationEventPublisher eventPublisher;
	
	public SecurityTokenProvider(SecurityTokenCache securityTokenCache,
			ApplicationEventPublisher eventPublisher) {
		this.securityTokenCache = securityTokenCache;
		this.eventPublisher = eventPublisher;
	}

	public String generate(int userId, String email) {
		var tokenBuilder = new StringBuilder();
		var random = new Random();
		for (int i = 0; i < 17; i++) {
			tokenBuilder.append((char) (random.nextInt(93) + 33));
		}
		var token = tokenBuilder.toString();
		
		securityTokenCache.store(userId, token);
		
		eventPublisher.publishEvent(new SecurityTokenGenerationEvent(this, token, userId, email));
		
		logger.info("Generated, stored and sent a security token for the user with email: {}", email);
		
		return token;
	}
	
	public Optional<String> find(int userId) {
		return securityTokenCache.find(userId);
	}
	
	public void delete(int userId) {
		securityTokenCache.delete(userId);
	}
}