package pl.konradmaksymilian.turnbasedgames.user.data.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.github.benmanes.caffeine.cache.Cache;

@Repository
public class SecurityTokenCache {
	
	private Cache<Integer, String> securityTokenCache;
	
	public SecurityTokenCache(Cache<Integer, String> securityTokenCache) {
		this.securityTokenCache = securityTokenCache;
	}

	public Optional<String> find(Integer userId) {
		return Optional.ofNullable(securityTokenCache.getIfPresent(userId));
	}
	
	public void store(Integer userId, String securityToken) {
		securityTokenCache.put(userId, securityToken);
	}
	
	public void delete(Integer userId) {
		securityTokenCache.invalidate(userId);
	}
}
