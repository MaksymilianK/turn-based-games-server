package pl.konradmaksymilian.turnbasedgames.core.exception;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException() {
		super("Player has not been found in the data base");
	}
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
}
