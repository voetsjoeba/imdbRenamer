package com.voetsjoeba.imdb.renamer.event;

import java.util.EventObject;

/**
 * Signifies that search results have been received by the model.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class SearchResultsSetEvent extends EventObject {

	public SearchResultsSetEvent(Object source) {
		super(source);
	}
	
}
