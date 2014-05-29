package com.voetsjoeba.imdb.renamer.event;

import java.util.EventObject;


/**
 * Generic event object that holds an object of a particular type as the source.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class GenericObjectEvent<T> extends EventObject {
	
	protected final T obj;
	
	public GenericObjectEvent(Object source, T obj) {
		super(source);
		this.obj = obj;
	}
	
	public T getObject() {
		return obj;
	}
	
}
