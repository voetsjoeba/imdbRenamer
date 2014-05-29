package com.voetsjoeba.imdb.renamer.domain.analysis;

import java.io.File;

import com.voetsjoeba.imdb.renamer.event.FilenameAnalysisListener;

/**
 * Extracts information about a title from an abstract file path ({@link File}) into {@link FilenameInfo}. Used to extract
 * a title's name, season/episode number, release group, etc. from a file name.
 * 
 * @author Jeroen De Ridder
 */
public interface FilenameAnalyzer {
	
	/**
	 * Inspects the provided file path <i>file</i> and attempts to extract various information about it,
	 * such as season/episode numbers in the case of series, the series/movie title, the year of release,
	 * release group, etc.
	 * @return A {@link FilenameInfo} object containing the information extracted from the title.
	 */
	public FilenameInfo getFilenameInfo(File file);
	
	/**
	 * Registers a listener for filename analysis-related events.
	 */
	public void addFilenameAnalysisListener(FilenameAnalysisListener listener);
	
	/**
	 * Removes a listener for filename analysis-related events.
	 */
	public void removeFilenameAnalysisListener(FilenameAnalysisListener listener);
	
}
