package com.voetsjoeba.imdb.renamer.model;

import javax.swing.event.EventListenerList;

import com.voetsjoeba.imdb.renamer.event.DefaultTitleRenameFormatChangedEvent;
import com.voetsjoeba.imdb.renamer.event.RenameFormatChangedEvent;
import com.voetsjoeba.imdb.renamer.event.RenameFormatListener;
import com.voetsjoeba.imdb.util.ListenerUtils;

/**
 * Holds the (default) title rename format, and notifies listeners of any changes.
 * 
 * @author Jeroen
 */
public class RenameFormatModel {
	
	// TODO: make defaultTitleRenameFormat nullable to indicate "use the same as renameFormat"
	// TODO: thread safety
	
	private static final String defaultRenameFormat = "${seriesTitle} - S${seasonNr:02}E${episodeNr:02} - ${episodeTitle}";
	private static final String defaultDefaultTitleRenameFormat =  "${seriesTitle} - S${seasonNr:02}E${episodeNr:02}";
	
	private String renameFormat;
	private String defaultTitleRenameFormat;
	
	private EventListenerList listeners;
	
	public RenameFormatModel(){
		
		listeners = new EventListenerList();
		
		renameFormat = defaultRenameFormat;
		defaultTitleRenameFormat = defaultDefaultTitleRenameFormat;
		
	}
	
	/**
	 * Returns the default rename format to be used.
	 * @see #getDefaultTitleRenameFormat()
	 */
	public String getRenameFormat() {
		return renameFormat;
	}
	
	/**
	 * Sets the rename format to be used. By default this rename format is used for all episodes. If you wish to use a custom
	 * format specifically for episodes that were assigned a generic title, you may do so using {@link #setDefaultTitleRenameFormat(String)}.
	 * @see #setDefaultTitleRenameFormat(String)
	 */
	public void setRenameFormat(String renameFormat) {
		
		if(renameFormat == null) return;
		
		if(!this.renameFormat.equals(renameFormat)){
			this.renameFormat = renameFormat;
			fireRenameFormatChanged(new RenameFormatChangedEvent(this));
		}
		
	}
	
	/**
	 * Gets the rename format to be used for episodes with generic titles.
	 * @see #getRenameFormat()
	 */
	public String getDefaultTitleRenameFormat() {
		return defaultTitleRenameFormat;
	}
	
	/**
	 * Sets a rename format to be used for episodes with generic titles. Once set, this format string will automatically be used
	 * for future renaming operations on episodes with a generic title, thereby overriding the fomat set through {@link #setRenameFormat(String)}.
	 * @see #setRenameFormat(String)
	 */
	public void setDefaultTitleRenameFormat(String defaultTitleRenameFormat) {
		
		if(defaultTitleRenameFormat == null) return;
		
		if(!this.defaultTitleRenameFormat.equals(defaultTitleRenameFormat)){
			this.defaultTitleRenameFormat = defaultTitleRenameFormat;
			fireDefaultTitleRenameFormatChanged(new DefaultTitleRenameFormatChangedEvent(this));
		}
		
	}
	
	/**
	 * Returns the default value of the rename format
	 */
	public String getDefaultRenameFormat() {
		return defaultRenameFormat;
	}
	
	/**
	 * Returns the default value of the defaultTitleRenameFormat
	 */
	public String getDefaultDefaultTitleRenameFormat() {
		return defaultDefaultTitleRenameFormat;
	}
	
	public void addRenameFormatListener(RenameFormatListener l){
		listeners.add(RenameFormatListener.class, l);
	}
	
	public void removeRenameFormatListener(RenameFormatListener l){
		listeners.remove(RenameFormatListener.class, l);
	}
	
	protected void fireDefaultTitleRenameFormatChanged(DefaultTitleRenameFormatChangedEvent e){
		for(RenameFormatListener l : listeners.getListeners(RenameFormatListener.class)){
			try {
				l.defaultTitleRenameFormatChanged(e);
			}
			catch(RuntimeException rex){
				ListenerUtils.handleListenerException(rex, l);
			}
		}
	}
	
	protected void fireRenameFormatChanged(RenameFormatChangedEvent e){
		for(RenameFormatListener l : listeners.getListeners(RenameFormatListener.class)){
			try {
				l.renameFormatChanged(e);
			}
			catch(RuntimeException rex){
				ListenerUtils.handleListenerException(rex, l);
			}
		}
	}
	
}
