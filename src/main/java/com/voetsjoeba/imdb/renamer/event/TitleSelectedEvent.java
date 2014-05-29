package com.voetsjoeba.imdb.renamer.event;

import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.model.TitleModel;

/**
 * Indicates that a {@link Title} was selected in the {@link TitleModel} or that the previous selection has changed.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class TitleSelectedEvent extends GenericObjectEvent<Title> {
	
	public TitleSelectedEvent(Object source, Title selectedTitle) {
		super(source, selectedTitle);
	}
	
}
