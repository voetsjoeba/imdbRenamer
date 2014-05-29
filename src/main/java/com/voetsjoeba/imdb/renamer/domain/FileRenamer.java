package com.voetsjoeba.imdb.renamer.domain;

import java.io.File;

import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.domain.analysis.FilenameAnalyzer;
import com.voetsjoeba.imdb.renamer.domain.exception.RenamingException;

/**
 * Represents an object capable of renaming files according to the information available in a {@link Title}.
 * 
 * @author Jeroen De Ridder
 */
public interface FileRenamer {
	
	/**
	 * Returns the new abstract filepath to which <i>file</i> should be renamed, based on the information from <i>title</i>.
	 * If no (valid) renamed filename could be determined, null may be returned. This method delegates to 
	 * {@link #getRenamedBasename(File, Title)} to determine the filename part of the new abstract file path.
	 * 
	 * @param file the original abstract file path to be renamed
	 * @param title the {@link Title} according to which the file should be renamed
	 * @see #getRenamedBasename(File, Title)
	 */
	public File getRenamedFile(File file, Title title) throws RenamingException;
	
	/**
	 * Returns the new basename (filename without extension) for <i>file</i>, based on the information from <i>title</i>.
	 * If no (valid) new basename could be determined, null may be returned.
	 * 
	 * @param file the original abstract file path to be renamed
	 * @param title the {@link Title} according to which the file should be renamed
	 * @throws RenamingException if an error occurs during renaming
	 */
	public String getRenamedBasename(File file, Title title) throws RenamingException;
	
	/**
	 * Returns the filename analyzer used by this renamer.
	 */
	public FilenameAnalyzer getFilenameAnalyzer();
	
	/**
	 * Sets the filename analyzer used by this renamer.
	 */
	public void setFilenameAnalyzer(FilenameAnalyzer filenameAnalyzer);
	
}
