package com.voetsjoeba.imdb.renamer.event;

import java.util.EventListener;

import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.model.TitleModel;

/**
 * Listener for changes to the {@link TitleModel}.
 * 
 * @author Jeroen De Ridder
 */
public interface TitleSelectionListener extends EventListener {
	
	/**
	 * Indicates that a {@link Title} was selected or that the previous selection has changed.
	 */
	public void titleSelected(TitleSelectedEvent e);
	
}
