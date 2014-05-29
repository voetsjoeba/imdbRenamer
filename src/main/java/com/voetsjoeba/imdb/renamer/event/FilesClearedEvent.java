package com.voetsjoeba.imdb.renamer.event;

import java.util.EventObject;

@SuppressWarnings("serial")
public class FilesClearedEvent extends EventObject {
	
	public FilesClearedEvent(Object source) {
		super(source);
	}
	
}
