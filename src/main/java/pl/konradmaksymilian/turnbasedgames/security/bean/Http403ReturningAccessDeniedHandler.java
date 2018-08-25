package pl.konradmaksymilian.turnbasedgames.security.bean;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class Http403ReturningAccessDeniedHandler implements AccessDeniedHandler {

	private ResponseWriter responseWriter;
	
	public Http403ReturningAccessDeniedHandler(ResponseWriter responseWriter) {
		this.responseWriter = responseWriter;
	}	
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		responseWriter.write(response, HttpStatus.FORBIDDEN, accessDeniedException.getMessage());
	}
}
