package com.voetsjoeba.imdb.renamer.domain.exception;

@SuppressWarnings("serial")
public class IllegalWindowsBasenameException extends IllegalFilenameException {
	
	public IllegalWindowsBasenameException() {
		super();
	}
	
	public IllegalWindowsBasenameException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IllegalWindowsBasenameException(String message) {
		super(message);
	}
	
	public IllegalWindowsBasenameException(Throwable cause) {
		super(cause);
	}
	
}
