package com.voetsjoeba.imdb.renamer.event;

import com.voetsjoeba.imdb.domain.LimitedTitle;
import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.registry.LimitedTitleRegistry;

/**
 * Signifies that a {@link Title} was removed from a {@link LimitedTitleRegistry}.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class RegistryTitleRemovedEvent extends GenericObjectEvent<LimitedTitle> {
	
	public RegistryTitleRemovedEvent(LimitedTitleRegistry source, LimitedTitle title) {
		super(source, title);
	}
	
}
