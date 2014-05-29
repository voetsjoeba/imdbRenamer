package com.voetsjoeba.imdb.renamer.gui;

import java.io.File;

/**
 * Represents a file chooser manager. Responsible for getting the user to select files and/or folders, and possibly persisting 
 * their last choice so as to improve usability.
 * 
 * @author Jeroen
 */
public interface FileChooserManager {
	
	public File[] selectFiles();
	
	public File selectFolder();
	
}