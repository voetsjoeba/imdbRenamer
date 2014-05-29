package com.voetsjoeba.imdb.renamer.event;

import java.util.EventObject;

import com.voetsjoeba.imdb.renamer.domain.analysis.FilenameAnalyzer;

/**
 * Fired when the available filename analysis information has been updated, e.g. when new filenames have been loaded and 
 * analyzed.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class FilenameAnalysisUpdatedEvent extends EventObject {
	
	public FilenameAnalysisUpdatedEvent(FilenameAnalyzer source) {
		super(source);
	}
	
}
