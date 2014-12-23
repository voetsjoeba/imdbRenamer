package com.voetsjoeba.imdb.renamer.gui.panel.files.list;

import java.io.File;

import javax.swing.DefaultListModel;

import com.voetsjoeba.imdb.renamer.event.FileAddedEvent;
import com.voetsjoeba.imdb.renamer.event.FileListListener;
import com.voetsjoeba.imdb.renamer.event.FileRemovedEvent;
import com.voetsjoeba.imdb.renamer.event.FilesClearedEvent;

@SuppressWarnings("serial")
public class FileListModel extends DefaultListModel<File> implements FileListListener {
	
	public FileListModel() {
		
	}
	
	@Override
	public void fileAdded(FileAddedEvent e) {
		addElement(e.getObject());
	}
	
	@Override
	public void fileRemoved(FileRemovedEvent e) {
		removeElement(e.getObject());
	}
	
	@Override
	public void filesCleared(FilesClearedEvent e) {
		clear();
	}
	
}
