package com.voetsjoeba.imdb.renamer.event;

import java.util.EventObject;

import com.voetsjoeba.imdb.renamer.model.RenameFormatModel;

@SuppressWarnings("serial")
public class RenameFormatChangedEvent extends EventObject {
	
	public RenameFormatChangedEvent(RenameFormatModel source) {
		super(source);
	}
	
}
