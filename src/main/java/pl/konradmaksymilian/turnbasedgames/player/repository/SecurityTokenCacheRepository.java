package pl.konradmaksymilian.turnbasedgames.player.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.github.benmanes.caffeine.cache.Cache;

@Repository
public class SecurityTokenCacheRepository {
	
	private Cache<Integer, String> securityTokenCache;
	
	public SecurityTokenCacheRepository(Cache<Integer, String> securityTokenCache) {
		this.securityTokenCache = securityTokenCache;
	}

	public Optional<String> find(Integer playerId) {
		return Optional.ofNullable(securityTokenCache.getIfPresent(playerId));
	}
	
	public void store(Integer playerId, String securityToken) {
		securityTokenCache.put(playerId, securityToken);
	}
	
	public void delete(Integer playerId) {
		securityTokenCache.invalidate(playerId);
	}
}
