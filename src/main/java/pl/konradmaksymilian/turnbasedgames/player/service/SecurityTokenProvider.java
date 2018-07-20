package pl.konradmaksymilian.turnbasedgames.player.service;

import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import pl.konradmaksymilian.turnbasedgames.player.event.SecurityTokenGenerationEvent;
import pl.konradmaksymilian.turnbasedgames.player.repository.SecurityTokenCacheRepository;

@Service
public class SecurityTokenProvider {

	private final Logger logger = LoggerFactory.getLogger(SecurityTokenProvider.class);
	
	private SecurityTokenCacheRepository securityTokenCacheRepository;
		
	private ApplicationEventPublisher eventPublisher;
	
	public SecurityTokenProvider(SecurityTokenCacheRepository securityTokenCacheRepository,
			ApplicationEventPublisher eventPublisher) {
		this.securityTokenCacheRepository = securityTokenCacheRepository;
		this.eventPublisher = eventPublisher;
	}

	public String generate(int playerId, String email) {
		var tokenBuilder = new StringBuilder();
		var random = new Random();
		for (int i = 0; i < 17; i++) {
			tokenBuilder.append((char) (random.nextInt(93) + 33));
		}
		var token = tokenBuilder.toString();
		
		securityTokenCacheRepository.store(playerId, token);
		
		eventPublisher.publishEvent(new SecurityTokenGenerationEvent(this, token, playerId, email));
		
		logger.info("Generated, stored and sent a security token for the player with email: {}", email);
		
		return token;
	}
	
	public Optional<String> find(int playerId) {
		return securityTokenCacheRepository.find(playerId);
	}
	
	public void delete(int playerId) {
		securityTokenCacheRepository.delete(playerId);
	}
}