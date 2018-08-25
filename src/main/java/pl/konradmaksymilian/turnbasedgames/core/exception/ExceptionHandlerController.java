package pl.konradmaksymilian.turnbasedgames.core.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pl.konradmaksymilian.turnbasedgames.core.dto.TextResponseDto;

@RestControllerAdvice
public class ExceptionHandlerController {
	
	private final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<TextResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception) {
		return createResponse(HttpStatus.NOT_FOUND, exception.getMessage());
	}
	
	@ExceptionHandler(ResourceConflictException.class)
	public ResponseEntity<TextResponseDto> handleResourceConflictException(ResourceConflictException exception) {
		return createResponse(HttpStatus.CONFLICT, exception.getMessage());
	}
	
	@ExceptionHandler(BadOperationException.class)
	public ResponseEntity<TextResponseDto> handleBadOperationException(Exception exception) {
		return createResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<TextResponseDto> handleValidationException(ValidationException exception) {
		var description = exception.getMessage().substring(exception.getMessage().indexOf(':') + 2);
		return createResponse(HttpStatus.BAD_REQUEST, description);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<TextResponseDto> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException exception) {
		var description = new StringBuilder();
		for (ObjectError error : exception.getBindingResult().getAllErrors()) {
			if (error instanceof FieldError) {
				description.append(((FieldError) error).getField());
			} else {
				description.append(error.getObjectName());
			}
			description.append(": ");
			description.append(error.getDefaultMessage()).append(", ");
		}
		description.delete(description.length() - 2, description.length());
		
		return createResponse(HttpStatus.BAD_REQUEST, description.toString());
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<TextResponseDto> handleAccessDeniedException(AccessDeniedException exception) {
		return createResponse(HttpStatus.FORBIDDEN, exception.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<TextResponseDto> handleAnotherException(Exception exception) {
		logger.error("An unexpected exception occured", exception);
		return createResponse(HttpStatus.FORBIDDEN, "An unexpected exception occured");
	}
	
	private ResponseEntity<TextResponseDto> createResponse(HttpStatus status, String message) {
		return ResponseEntity.status(status)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(new TextResponseDto(message));
	}
}