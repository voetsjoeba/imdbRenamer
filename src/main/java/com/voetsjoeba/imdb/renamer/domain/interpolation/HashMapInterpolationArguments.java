package com.voetsjoeba.imdb.renamer.domain.interpolation;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Fairly straightforward implementation of {@link InterpolationArguments} that uses a {@link HashMap} as its
 * underlying structure.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class HashMapInterpolationArguments extends HashMap<String, Object> implements InterpolationArguments {
	
	@Override
	public boolean add(String key, Object value) {
		return (super.put(key, value) == null);
	}
	
	@Override
	public boolean containsKey(String key) {
		return super.containsKey(key);
	}
	
	@Override
	public Iterator<String> getKeyIterator() {
		return super.keySet().iterator();
	}
	
	@Override
	public Object getValue(String key) {
		return super.get(key);
	}
	
}
