package com.voetsjoeba.imdb.renamer.gui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.event.FileAddedEvent;
import com.voetsjoeba.imdb.renamer.event.FileListListener;
import com.voetsjoeba.imdb.renamer.event.FileRemovedEvent;
import com.voetsjoeba.imdb.renamer.event.FilesClearedEvent;
import com.voetsjoeba.imdb.renamer.event.TitleSelectedEvent;
import com.voetsjoeba.imdb.renamer.event.TitleSelectionListener;
import com.voetsjoeba.imdb.renamer.gui.Images;
import com.voetsjoeba.imdb.renamer.gui.action.generic.EnabledStatusAction;
import com.voetsjoeba.imdb.renamer.model.FilesModel;
import com.voetsjoeba.imdb.renamer.model.TitleModel;

/**
 * Removes those files from the file list that will not be affected by the current renaming operation.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class RemoveUnaffectedFilesAction extends EnabledStatusAction implements TitleSelectionListener, FileListListener {
	
	public RemoveUnaffectedFilesAction(){
		super("Remove Unaffected", Images.getRemoveUnaffectedTitlesIcon());
		setTooltip("Removes files that are unaffected by the current renaming operation.");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(!isEnabled()) return;
		
		FilesModel filesModel = Application.getInstance().getFilesModel();
		TitleModel titleModel = Application.getInstance().getTitleModel();
		
		//FileRenamer fileRenamer = Application.getInstance().getRenamer();
		
		Set<File> removeSet = new HashSet<File>();
		
		for(File file : filesModel){
			
			if(Application.getInstance().isUnaffectedRenameCandidate(file, titleModel.getTitle())){
				removeSet.add(file);
			}
			
		}

		filesModel.remove(removeSet);
		
	}
	
	@Override
	public void updateEnabledStatus() {
		
		TitleModel titleModel = Application.getInstance().getTitleModel();
		FilesModel filesModel = Application.getInstance().getFilesModel();
		
		setEnabled(titleModel.getTitle() != null && filesModel.getSize() > 0);
		
	}
	
	@Override
	public void titleSelected(TitleSelectedEvent e) {
		updateEnabledStatus();
	}
	
	@Override
	public void fileAdded(FileAddedEvent e) {
		updateEnabledStatus();
	}
	
	@Override
	public void fileRemoved(FileRemovedEvent e) {
		updateEnabledStatus();
	}
	
	@Override
	public void filesCleared(FilesClearedEvent e) {
		updateEnabledStatus();
	}
	
}
