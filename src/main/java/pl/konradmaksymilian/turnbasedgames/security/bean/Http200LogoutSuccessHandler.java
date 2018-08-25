package pl.konradmaksymilian.turnbasedgames.security.bean;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class Http200LogoutSuccessHandler implements LogoutSuccessHandler {

	private ResponseWriter responseWriter;
	
	public Http200LogoutSuccessHandler(ResponseWriter responseWriter) {
		this.responseWriter = responseWriter;
	}
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		responseWriter.write(response, HttpStatus.OK, "User has been successfully logged out!");
	}
}
