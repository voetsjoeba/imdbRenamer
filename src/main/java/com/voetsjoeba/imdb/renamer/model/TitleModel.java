package com.voetsjoeba.imdb.renamer.model;

import javax.swing.event.EventListenerList;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import com.voetsjoeba.imdb.domain.AbstractTitle;
import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.event.TitleSelectedEvent;
import com.voetsjoeba.imdb.renamer.event.TitleSelectionListener;
import com.voetsjoeba.imdb.util.ListenerUtils;

/**
 * Holds the currently loaded IMDb {@link AbstractTitle}.
 * 
 * @author Jeroen De Ridder
 */
@ThreadSafe
public class TitleModel implements ApplicationModel {
	
	protected Title title;
	private EventListenerList listeners;
	
	public TitleModel(){
		listeners = new EventListenerList();
	}
	
	@GuardedBy("this")
	public synchronized Title getTitle() {
		return title;
	}
	
	/**
	 * Returns true if there is currently a Title selected, false otherwise.
	 */
	@GuardedBy("this")
	public synchronized boolean isTitleSelected(){
		return title != null;
	}
	
	public void setTitle(Title newTitle) {
		
		boolean titleUpdated = false;
		
		synchronized(this){
			if(title == null || !title.equals(newTitle)){
				title = newTitle;
				titleUpdated = true;
			}
		}
		
		if(titleUpdated){
			fireTitleSelected(new TitleSelectedEvent(this, newTitle));
		}
		
	}
	
	public void addTitleSelectionListener(TitleSelectionListener listener){
		listeners.add(TitleSelectionListener.class, listener);
	}
	
	public void removeTitleSelectionListener(TitleSelectionListener listener){
		listeners.remove(TitleSelectionListener.class, listener);
	}
	
	private void fireTitleSelected(TitleSelectedEvent e) {
		for(TitleSelectionListener l : listeners.getListeners(TitleSelectionListener.class)){
			try {
				l.titleSelected(e);
			}
			catch(RuntimeException rex) {
				ListenerUtils.handleListenerException(rex, l);
			}
		}
	}
	
}
