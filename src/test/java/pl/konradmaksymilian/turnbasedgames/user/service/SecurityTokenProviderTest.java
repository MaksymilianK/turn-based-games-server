package pl.konradmaksymilian.turnbasedgames.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import pl.konradmaksymilian.turnbasedgames.user.event.SecurityTokenGenerationEvent;
import pl.konradmaksymilian.turnbasedgames.user.repository.SecurityTokenCacheRepository;
import pl.konradmaksymilian.turnbasedgames.user.service.SecurityTokenProvider;

public class SecurityTokenProviderTest {
	
	private SecurityTokenProvider securityTokenProvider;
	
	@Mock
	private SecurityTokenCacheRepository securityTokenCacheRepository;
	
	@Mock
	private ApplicationEventPublisher eventPublisher;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		securityTokenProvider = new SecurityTokenProvider(securityTokenCacheRepository, eventPublisher);
	}
	
	@Test
	public void generateAndStore_generatesAndStoresSecurityToken() {
		var token = securityTokenProvider.generate(1, "email");
		var characters = token.toCharArray();
		
		var hasValidChars = true;
		for (var character : characters) {
			if (character < 33 || character > 125) {
				hasValidChars = false;
				break;
			}
		}
		
		assertThat(token).hasSize(17);
		assertThat(hasValidChars).isTrue();
		verify(securityTokenCacheRepository, times(1)).store(1, token);
		verify(eventPublisher, times(1)).publishEvent(any(SecurityTokenGenerationEvent.class));
	}
}
