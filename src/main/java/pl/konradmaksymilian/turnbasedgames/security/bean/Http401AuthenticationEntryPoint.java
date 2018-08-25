package pl.konradmaksymilian.turnbasedgames.security.bean;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class Http401AuthenticationEntryPoint implements AuthenticationEntryPoint {

	private ResponseWriter responseWriter;
	
	public Http401AuthenticationEntryPoint(ResponseWriter responseWriter) {
		this.responseWriter = responseWriter;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		responseWriter.write(response, HttpStatus.UNAUTHORIZED, "The current user is not authenticated!");
	}
}
