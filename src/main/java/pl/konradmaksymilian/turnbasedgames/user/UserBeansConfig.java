package pl.konradmaksymilian.turnbasedgames.user;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class UserBeansConfig {

	@Bean
	public Cache<Integer, String> securityTokenCache() {
		return Caffeine.newBuilder()
				.expireAfterWrite(24, TimeUnit.HOURS)
				.build();
	}
}
