package com.voetsjoeba.imdb.renamer.domain.exception;

/**
 * Indicates that an interpolation operation was aborted because it was detected that it would be interpolating infinitively.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class InfiniteInterpolationException extends InterpolationException {

	public InfiniteInterpolationException() {
		super();
	}

	public InfiniteInterpolationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InfiniteInterpolationException(String message) {
		super(message);
	}

	public InfiniteInterpolationException(Throwable cause) {
		super(cause);
	}
	
}
