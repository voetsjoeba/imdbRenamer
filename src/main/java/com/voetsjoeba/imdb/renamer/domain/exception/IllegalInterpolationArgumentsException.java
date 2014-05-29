package com.voetsjoeba.imdb.renamer.domain.exception;


/**
 * Indicates that illegal arguments were provided to a variable.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class IllegalInterpolationArgumentsException extends InterpolationException {
	
	public IllegalInterpolationArgumentsException() {
		super();
	}
	
	public IllegalInterpolationArgumentsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IllegalInterpolationArgumentsException(String message) {
		super(message);
	}
	
	public IllegalInterpolationArgumentsException(Throwable cause) {
		super(cause);
	}
	
}
