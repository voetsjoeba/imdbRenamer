package com.voetsjoeba.imdb.renamer.domain.analysis;

import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voetsjoeba.imdb.renamer.event.FilenameAnalysisListener;
import com.voetsjoeba.imdb.renamer.event.FilenameAnalysisUpdatedEvent;
import com.voetsjoeba.imdb.util.ListenerUtils;

/**
 * Abstract superclass for FilenameAnalyzer implementations. Provides listener handling.
 * 
 * @author Jeroen
 */
public abstract class AbstractFilenameAnalyzer implements FilenameAnalyzer {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(AbstractFilenameAnalyzer.class);
	protected EventListenerList listeners;
	
	public AbstractFilenameAnalyzer(){
		listeners = new EventListenerList();
	}
	
	@Override public void addFilenameAnalysisListener(FilenameAnalysisListener listener) {
		listeners.add(FilenameAnalysisListener.class, listener);
	}

	@Override public void removeFilenameAnalysisListener(FilenameAnalysisListener listener) {
		listeners.remove(FilenameAnalysisListener.class, listener);
	}
	
	protected void fireFilenameAnalysisChanged(FilenameAnalysisUpdatedEvent e){
		for(FilenameAnalysisListener l : listeners.getListeners(FilenameAnalysisListener.class)){
			try {
				l.filenameAnalysisUpdated(e);
			}
			catch(RuntimeException rex){
				ListenerUtils.handleListenerException(rex, l);
			}
		}
	}
	
}
