package com.voetsjoeba.imdb.renamer.domain.exception;

import com.voetsjoeba.imdb.renamer.domain.interpolation.StringInterpolator;

/**
 * Generic superclass for exceptions that occur during string interpolation.
 * 
 * @see StringInterpolator
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class InterpolationException extends Exception {
	
	public InterpolationException() {
		super();
	}
	
	public InterpolationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InterpolationException(String message) {
		super(message);
	}
	
	public InterpolationException(Throwable cause) {
		super(cause);
	}
	
}
