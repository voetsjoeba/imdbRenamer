package com.voetsjoeba.imdb.renamer.domain.exception;

import com.voetsjoeba.imdb.renamer.model.TitleModel;

/**
 * Indicates that an episode does not exist within the currently selected title.
 * 
 * @see TitleModel
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class NoSuchEpisodeException extends RenamingException { // TODO: should this really extend renamingexception?
	
	public NoSuchEpisodeException() {
		super();
	}
	
	public NoSuchEpisodeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoSuchEpisodeException(String message) {
		super(message);
	}
	
	public NoSuchEpisodeException(Throwable cause) {
		super(cause);
	}
	
}
