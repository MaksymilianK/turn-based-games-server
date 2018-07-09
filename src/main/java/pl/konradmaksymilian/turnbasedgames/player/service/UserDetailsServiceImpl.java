package pl.konradmaksymilian.turnbasedgames.player.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pl.konradmaksymilian.turnbasedgames.player.repository.PlayerRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private PlayerRepository playerRepository;

	public UserDetailsServiceImpl(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return playerRepository.findByNick(username).orElseThrow(() -> new UsernameNotFoundException(
				"Cannot find the player with the given username: '" + username + "' in the data base"));
	}
}
