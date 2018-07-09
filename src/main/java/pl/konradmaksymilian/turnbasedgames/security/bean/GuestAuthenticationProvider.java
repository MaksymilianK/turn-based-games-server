package pl.konradmaksymilian.turnbasedgames.security.bean;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.player.Role;
import pl.konradmaksymilian.turnbasedgames.player.entity.Player;

@Component
public class GuestAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String nick = (String) authentication.getName();
		if (nick.isEmpty()) {
			return new UsernamePasswordAuthenticationToken(new Player("guest", "", "", Role.GUEST), "");
		} else {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
