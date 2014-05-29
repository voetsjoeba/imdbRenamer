package com.voetsjoeba.imdb.renamer.gui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collection;

import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.event.FileAddedEvent;
import com.voetsjoeba.imdb.renamer.event.FileListListener;
import com.voetsjoeba.imdb.renamer.event.FileRemovedEvent;
import com.voetsjoeba.imdb.renamer.event.FilenameAnalysisListener;
import com.voetsjoeba.imdb.renamer.event.FilenameAnalysisUpdatedEvent;
import com.voetsjoeba.imdb.renamer.event.FilesClearedEvent;
import com.voetsjoeba.imdb.renamer.event.TitleSelectedEvent;
import com.voetsjoeba.imdb.renamer.event.TitleSelectionListener;
import com.voetsjoeba.imdb.renamer.gui.Images;
import com.voetsjoeba.imdb.renamer.gui.action.generic.EnabledStatusAction;
import com.voetsjoeba.imdb.renamer.model.FilesModel;
import com.voetsjoeba.imdb.renamer.model.TitleModel;

/**
 * Action to perform the renaming of the loaded files according to the currently selected {@link Title}.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class PerformRenameAction extends EnabledStatusAction implements FileListListener, TitleSelectionListener, FilenameAnalysisListener {
	
	private Collection<File> files;
	private static final String baseName = "Perform Rename";
	
	public PerformRenameAction(Collection<File> files){
		
		super(baseName, Images.getRenameIcon());
		
		this.files = files;
		setTooltip("Rename the currently selected file" + (files == null || files.size() != 1 ? "s" : "") + ".");
		
		updateEnabledStatus();
		
	}
	
	public PerformRenameAction(){
		this(null);
		setTooltip("Rename the currently loaded files (where applicable).");
	}
	
	@Override
	public void updateEnabledStatus(){
		
		String newName = baseName;
		boolean newEnabledStatus = false; 
		
		FilesModel filesModel = Application.getInstance().getFilesModel();
		TitleModel titleModel = Application.getInstance().getTitleModel();
		
		Title selectedTitle = titleModel.getTitle();
		if(selectedTitle != null){
			
			//setEnabled(filesModel.getSize() > 0 && titleModel.getTitle() != null);
			
			// check which files need renaming and can be renamed
			int properRenameCandidates = 0;
			
			// if a list of files were passed, consider only that list; otherwise, use the entire file list
			Iterable<File> candidates = files;
			if(candidates == null) candidates = filesModel;
			
			for(File file : candidates){
				
				if(Application.getInstance().isProperRenameCandidate(file, selectedTitle)){
					properRenameCandidates++;
				}
				
			}
			
			if(properRenameCandidates > 0){
				newEnabledStatus = true;
				newName += " (" + properRenameCandidates + ")";
			}
			
		}
		
		setName(newName);
		setEnabled(newEnabledStatus);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO: add confirmation dialog (+ add option to disable it)
		Application.getInstance().performRename(files);
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
	public void titleSelected(TitleSelectedEvent e) {
		updateEnabledStatus();
	}
	
	@Override
	public void fileRemoved(FileRemovedEvent e) {
		updateEnabledStatus();
	}
	
	@Override
	public void filenameAnalysisUpdated(FilenameAnalysisUpdatedEvent e) {
		updateEnabledStatus();
	}
	
}
