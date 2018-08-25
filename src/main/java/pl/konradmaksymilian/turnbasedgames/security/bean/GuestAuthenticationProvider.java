package pl.konradmaksymilian.turnbasedgames.security.bean;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import pl.konradmaksymilian.turnbasedgames.user.Role;
import pl.konradmaksymilian.turnbasedgames.user.entity.User;

@Component
public class GuestAuthenticationProvider implements AuthenticationProvider {
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		var nick = (String) authentication.getName();
		if (nick.isEmpty()) {
			var user = new User("guest", "", "", Role.GUEST);
			return new UsernamePasswordAuthenticationToken(user, "password", new ArrayList<>(user.getAuthorities()));
		} else {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
