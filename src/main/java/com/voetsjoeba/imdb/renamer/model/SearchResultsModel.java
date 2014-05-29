package com.voetsjoeba.imdb.renamer.model;

import java.util.Collections;
import java.util.List;

import javax.swing.event.EventListenerList;

import com.voetsjoeba.imdb.domain.LimitedTitle;
import com.voetsjoeba.imdb.renamer.event.SearchResultsListener;
import com.voetsjoeba.imdb.renamer.event.SearchResultsSetEvent;
import com.voetsjoeba.imdb.util.ListenerUtils;

public class SearchResultsModel implements ApplicationModel {
	
	private EventListenerList listeners;
	protected List<LimitedTitle> searchResults;
	
	public SearchResultsModel() {
		listeners = new EventListenerList();
		searchResults = Collections.emptyList(); // so we can use it to synchronize on
	}
	
	public List<LimitedTitle> getSearchResults() {
		synchronized(searchResults){
			return searchResults;
		}
	}

	public void setSearchResults(List<LimitedTitle> searchResults) {
		
		boolean resultsUpdated = false;
		
		synchronized(this.searchResults){
			if(this.searchResults != searchResults) {
				this.searchResults = searchResults;
				resultsUpdated = true;
			}
		}
		
		if(resultsUpdated) fireResultsSet(new SearchResultsSetEvent(this));
		
	}
	
	public void addSearchResultsListener(SearchResultsListener listener) {
		listeners.add(SearchResultsListener.class, listener);
	}
	
	protected void fireResultsSet(SearchResultsSetEvent e) {
		for (SearchResultsListener listener : listeners.getListeners(SearchResultsListener.class)) {
			try {
				listener.searchResultsSet(e);
			}
			catch(RuntimeException rex) {
				ListenerUtils.handleListenerException(rex, listener);
			}
		}
	}
	
}
