package com.voetsjoeba.imdb.renamer.event;

import java.util.EventObject;

import com.voetsjoeba.imdb.renamer.model.RenameFormatModel;

@SuppressWarnings("serial")
public class DefaultTitleRenameFormatChangedEvent extends EventObject {
	
	public DefaultTitleRenameFormatChangedEvent(RenameFormatModel source) {
		super(source);
	}
	
}
