package com.voetsjoeba.imdb.renamer.event;

import java.io.File;

@SuppressWarnings("serial")
public class FileAddedEvent extends GenericObjectEvent<File> {
	
	public FileAddedEvent(Object source, File file) {
		super(source, file);
	}
	
}
