package com.voetsjoeba.imdb.renamer.event;

import java.util.EventListener;

public interface PrefetchListener extends EventListener {
	
	/**
	 * Fired when the prefetching has progressed to a new level of progress.
	 */
	public void prefetchProgress(PrefetchProgressEvent e);
	
}
