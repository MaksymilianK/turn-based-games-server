package pl.konradmaksymilian.turnbasedgames.security.bean;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.konradmaksymilian.turnbasedgames.core.dto.TextResponseDto;

@Component
public class ResponseWriter {
	
	private ObjectMapper objectMapper;
	
	public ResponseWriter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	public void write(HttpServletResponse response, HttpStatus httpStatus, String message) 
			throws JsonProcessingException, IOException {
		response.setStatus(httpStatus.value());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.getWriter().print(objectMapper.writeValueAsString(new TextResponseDto(message)));
	}
}
