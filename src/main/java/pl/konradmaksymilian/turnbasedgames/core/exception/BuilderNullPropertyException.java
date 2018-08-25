package pl.konradmaksymilian.turnbasedgames.core.exception;

public class BuilderNullPropertyException extends RuntimeException {

	public BuilderNullPropertyException(Class<?> clazz) {
		super("Cannot build a " + clazz.getName() + " because one of its properties is missing");
	}
	
	public BuilderNullPropertyException(String message) {
		super(message);
	}
}
