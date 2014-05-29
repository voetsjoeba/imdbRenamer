package com.voetsjoeba.imdb.renamer.event;

import com.voetsjoeba.imdb.domain.LimitedTitle;
import com.voetsjoeba.imdb.renamer.registry.LimitedTitleRegistry;

/**
 * Indicates that a new {@link LimitedTitle} was added to a {@link LimitedTitleRegistry}.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class RegistryTitleAddedEvent extends GenericObjectEvent<LimitedTitle> {
	
	public RegistryTitleAddedEvent(LimitedTitleRegistry source, LimitedTitle title) {
		super(source, title);
	}
	
}
