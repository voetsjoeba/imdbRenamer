package com.voetsjoeba.imdb.renamer.domain.exception;


@SuppressWarnings("serial")
public class IllegalInterpolationValueException extends InterpolationException {
	
	public IllegalInterpolationValueException() {
		super();
	}
	
	public IllegalInterpolationValueException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IllegalInterpolationValueException(String message) {
		super(message);
	}
	
	public IllegalInterpolationValueException(Throwable cause) {
		super(cause);
	}
	
}
