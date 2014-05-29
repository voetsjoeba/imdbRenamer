package com.voetsjoeba.imdb.renamer.event;

import java.util.EventListener;

import com.voetsjoeba.imdb.renamer.model.SearchResultsModel;

/**
 * Listener for changes to the {@link SearchResultsModel}.
 * 
 * @author Jeroen
 */
public interface SearchResultsListener extends EventListener {
	
	/**
	 * Indicates that search results were loaded into the model.
	 */
	public void searchResultsSet(SearchResultsSetEvent e);
	
}
