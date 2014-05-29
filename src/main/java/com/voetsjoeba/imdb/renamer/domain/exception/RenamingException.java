package com.voetsjoeba.imdb.renamer.domain.exception;

/**
 * Indicates a problem during a file renaming operation.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class RenamingException extends Exception {
	
	public RenamingException() {
		super();
	}
	
	public RenamingException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RenamingException(String message) {
		super(message);
	}
	
	public RenamingException(Throwable cause) {
		super(cause);
	}
	
}
