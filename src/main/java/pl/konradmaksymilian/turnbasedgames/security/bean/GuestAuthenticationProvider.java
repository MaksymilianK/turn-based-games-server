package pl.konradmaksymilian.turnbasedgames.security.bean;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.player.Role;
import pl.konradmaksymilian.turnbasedgames.player.entity.Player;

@Component
public class GuestAuthenticationProvider implements AuthenticationProvider {

	private static volatile AtomicInteger guestCounter = new AtomicInteger(0);
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		var nick = (String) authentication.getName();
		if (nick.isEmpty()) {
			return new UsernamePasswordAuthenticationToken(new Player("guest" + guestCounter.getAndIncrement(), "", "",
					Role.GUEST), "");
		} else {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
