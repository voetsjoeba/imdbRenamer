package com.voetsjoeba.imdb.renamer.event;

import java.util.EventListener;

/**
 * Listener for filename analysis events.
 * 
 * @author Jeroen
 */
public interface FilenameAnalysisListener extends EventListener {
	
	/**
	 * Indicates that a file to season/episode number mapping has been updated.
	 */
	public void filenameAnalysisUpdated(FilenameAnalysisUpdatedEvent e);
	
}
