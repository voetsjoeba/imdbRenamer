package com.voetsjoeba.imdb.renamer.registry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import com.voetsjoeba.imdb.domain.LimitedTitle;
import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.event.LimitedTitleRegistryListener;

/**
 * Registry for saving commonly used title names/title ID pairs, to prevent unnecessary future lookups.
 * Note that the episode titles need not be saved, as those will only become outdated anyway.
 * 
 * @author Jeroen De Ridder
 */
public interface LimitedTitleRegistry extends Iterable<LimitedTitle> {
	
	/**
	 * Adds a title to the cache.
	 */
	public void add(LimitedTitle title);
	
	/**
	 * Adds all titles in the collection to the cache.
	 */
	public void add(Collection<LimitedTitle> titles);
	
	/**
	 * Removes a title from the cache.
	 */
	public void remove(LimitedTitle title);
	
	/**
	 * Removes all titles in the collection from the cache.
	 */
	public void remove(Collection<LimitedTitle> titles);
	
	/**
	 * Returns true if the cache contains a title with the provided id, false otherwise.
	 * @param titleId The ID of the title to check for
	 */
	public boolean contains(String titleId);
	
	/**
	 * Returns the {@link Title} with the provided id from the cache, or null if it's not in the cache.
	 * @param titleId The title ID to look up
	 */
	public LimitedTitle get(String titleId);
	
	/**
	 * Returns the amount of titles in this registry.
	 */
	public int size();
	
	/**
	 * Returns titles maintained by this cache.
	 */
	public Collection<LimitedTitle> getTitles();
	
	// -------------------------------------------------------------------------------------------------
	
	/**
	 * Reads a cache from a file.
	 */
	public void read(File in) throws FileNotFoundException, IOException;
	
	/**
	 * Writes the cache to a file.
	 */
	public void write(File out) throws FileNotFoundException, IOException;
	
	/**
	 * Returns true if there have been changes to this registry, false otherwise.
	 */
	public boolean isDirty();
	
	// -------------------------------------------------------------------------------------------------
	
	/**
	 * Adds a {@link LimitedTitleRegistryListener} to be notified of events.
	 */
	public void addLimitedTitleRegistryListener(LimitedTitleRegistryListener l);
	
	/**
	 * Removes a {@link LimitedTitleRegistryListener} from the listener list.
	 */
	public void removeLimitedTitleRegistryListener(LimitedTitleRegistryListener l);
	
	// -------------------------------------------------------------------------------------------------
	
	/**
	 * Attempts to find a set of LimitedTitles whose titles approximately match the input file name.
	 */
	public Set<LimitedTitle> getApproximateMatches(String filename);
	
}