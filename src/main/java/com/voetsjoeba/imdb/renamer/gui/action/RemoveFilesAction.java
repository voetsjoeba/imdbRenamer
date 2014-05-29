package com.voetsjoeba.imdb.renamer.gui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collection;
import java.util.Collections;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.gui.Images;
import com.voetsjoeba.imdb.renamer.gui.action.generic.ApplicationAction;
import com.voetsjoeba.imdb.renamer.model.FilesModel;

/**
 * Removes one or more files from the file list.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class RemoveFilesAction extends ApplicationAction {
	
	protected Collection<File> filesToRemove;
	
	public RemoveFilesAction(Collection<File> filesToRemove){
		
		super("Remove", Images.getRemoveFileIcon());
		this.filesToRemove = filesToRemove;
		
		setTooltip("Removes file(s).");
		
	}
	
	@SuppressWarnings("unchecked")
	public RemoveFilesAction(){
		this(Collections.EMPTY_LIST);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		FilesModel filesModel = Application.getInstance().getFilesModel();
		filesModel.remove(filesToRemove);
		
	}
	
}
