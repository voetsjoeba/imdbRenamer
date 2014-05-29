package com.voetsjoeba.imdb.renamer.gui.action;

import java.awt.event.ActionEvent;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.event.FileAddedEvent;
import com.voetsjoeba.imdb.renamer.event.FileListListener;
import com.voetsjoeba.imdb.renamer.event.FileRemovedEvent;
import com.voetsjoeba.imdb.renamer.event.FilesClearedEvent;
import com.voetsjoeba.imdb.renamer.gui.action.generic.EnabledStatusAction;
import com.voetsjoeba.imdb.renamer.model.FilesModel;

/**
 * Removes all currently loaded files.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class ClearFilesAction extends EnabledStatusAction implements FileListListener {
	
	public ClearFilesAction(){
		super("Clear");
		setTooltip("Removes all currently loaded files.");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Application.getInstance().getFilesModel().clear();
	}
	
	@Override
	public void updateEnabledStatus(){
		FilesModel model = Application.getInstance().getFilesModel();
		setEnabled(model.getSize() > 0);
	}
	
	@Override
	public void fileAdded(FileAddedEvent e) {
		updateEnabledStatus();
	}
	
	@Override
	public void filesCleared(FilesClearedEvent e) {
		updateEnabledStatus();
	}

	@Override
	public void fileRemoved(FileRemovedEvent e) {
		updateEnabledStatus();
	}
	
}
