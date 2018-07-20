package pl.konradmaksymilian.turnbasedgames.player.configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class PlayerBeansConfig {

	@Bean
	public Cache<Integer, String> securityTokenCache() {
		return Caffeine.newBuilder()
				.expireAfterWrite(24, TimeUnit.HOURS)
				.build();
	}
}
