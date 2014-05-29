package com.voetsjoeba.imdb.renamer.event;

import java.util.EventObject;

import com.voetsjoeba.imdb.renamer.Application;

/**
 * Signifies that prefetching has made progress. Note that the progress is not included in this event object, as this allows
 * for out-of-order delivery of progress messages as threads continuously update the progress. Instead, the listener is 
 * invited to fetch the new progress state.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class PrefetchProgressEvent extends EventObject {
	
	public PrefetchProgressEvent(Application source) {
		super(source);
	}
	
	@Override public Application getSource() {
		return (Application) super.getSource();
	}
	
}
