package com.voetsjoeba.imdb.renamer.event;

import java.util.EventListener;

public interface RenameFormatListener extends EventListener {
	
	public void defaultTitleRenameFormatChanged(DefaultTitleRenameFormatChangedEvent e);
	public void renameFormatChanged(RenameFormatChangedEvent e);
	
}
