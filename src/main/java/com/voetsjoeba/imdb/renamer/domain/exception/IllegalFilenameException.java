package com.voetsjoeba.imdb.renamer.domain.exception;

@SuppressWarnings("serial")
public class IllegalFilenameException extends Exception {

	public IllegalFilenameException() {
		super();
	}

	public IllegalFilenameException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalFilenameException(String message) {
		super(message);
	}

	public IllegalFilenameException(Throwable cause) {
		super(cause);
	}
	
}
