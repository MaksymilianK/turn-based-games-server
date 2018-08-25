package pl.konradmaksymilian.turnbasedgames.security.bean;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class Http401AuthenticationFailureHandler implements AuthenticationFailureHandler {

	private ResponseWriter responseWriter;
	
	public Http401AuthenticationFailureHandler(ResponseWriter responseWriter) {
		this.responseWriter = responseWriter;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		responseWriter.write(response, HttpStatus.UNAUTHORIZED, "Authentication failure!");
	}
}
