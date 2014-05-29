package com.voetsjoeba.imdb.renamer.event;

import java.util.EventListener;

import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.registry.DefaultLimitedTitleRegistry;

/**
 * Listener for changes to a {@link DefaultLimitedTitleRegistry} object.
 * 
 * @author Jeroen De Ridder
 */
public interface LimitedTitleRegistryListener extends EventListener {
	
	/**
	 * Indicates that a new {@link Title} was added to the cache.
	 */
	public void registryTitleAdded(RegistryTitleAddedEvent e);
	
	/**
	 * Indicates that a {@link Title} was removed from the cache.
	 */
	public void registryTitleRemoved(RegistryTitleRemovedEvent e);
	
}
