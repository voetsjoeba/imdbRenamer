package com.voetsjoeba.imdb.renamer.domain.exception;

/**
 * Indicates that a season/episode number could not be extracted from an abstract file path.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class NoEpisodeMappingException extends RenamingException {
	
	public NoEpisodeMappingException() {
		super();
	}
	
	public NoEpisodeMappingException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoEpisodeMappingException(String message) {
		super(message);
	}
	
	public NoEpisodeMappingException(Throwable cause) {
		super(cause);
	}
	
}
