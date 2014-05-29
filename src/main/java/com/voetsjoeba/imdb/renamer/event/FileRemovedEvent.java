package com.voetsjoeba.imdb.renamer.event;

import java.io.File;

import com.voetsjoeba.imdb.renamer.model.FilesModel;

/**
 * Indicates that a file was removed from the {@link FilesModel}.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class FileRemovedEvent extends GenericObjectEvent<File> {
	
	public FileRemovedEvent(Object source, File file) {
		super(source, file);
	}
	
}
