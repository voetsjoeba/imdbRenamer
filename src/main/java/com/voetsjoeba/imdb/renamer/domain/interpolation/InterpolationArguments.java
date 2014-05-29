package com.voetsjoeba.imdb.renamer.domain.interpolation;

import java.util.Iterator;

/**
 * Represents and holds a set of key/value argument pairs.
 * 
 * @author Jeroen De Ridder
 */
public interface InterpolationArguments {
	
	/**
	 * Adds a new argument through a key/value pair. If the argument key did not previously exist, the argument is added and true is returned. Otherwise, false is returned.
	 */
	public boolean add(String key, Object value);
	
	/**
	 * Returns all argument names held by this instance.
	 */
	public Iterator<String> getKeyIterator();
	
	/**
	 * Returns the value for the provided key. If <i>key</i> does not exist, null is returned. Note that null may also be returned
	 * as the mapped value of an existing key; use {@link #containsKey(String)} to differentiate between the two cases.
	 */
	public Object getValue(String key);
	
	/**
	 * Returns true if an argument by the name of <i>key</i> exists.
	 */
	public boolean containsKey(String key);
	
}
