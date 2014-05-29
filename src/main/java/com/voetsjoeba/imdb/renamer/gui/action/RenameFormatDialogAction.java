package com.voetsjoeba.imdb.renamer.gui.action;

import java.awt.event.ActionEvent;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.gui.action.generic.ApplicationAction;
import com.voetsjoeba.imdb.renamer.gui.panel.rename.RenameFormatDialog;
import com.voetsjoeba.imdb.renamer.model.RenameFormatModel;

/**
 * Opens the rename formats dialog.
 * 
 * @see RenameFormatDialog
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class RenameFormatDialogAction extends ApplicationAction {
	
	public RenameFormatDialogAction() {
		super("Set Rename Format");
	}
	
	/*@Override
	public void updateEnabledStatus() {
		
	}*/
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		RenameFormatModel renameFormatModel = Application.getInstance().getRenameFormatModel();
		RenameFormatDialog formatDialog = new RenameFormatDialog();
		
		String renameFormat = renameFormatModel.getRenameFormat();
		String defaultTitleRenameFormat = renameFormatModel.getDefaultTitleRenameFormat();
		formatDialog.setRenameFormatString(renameFormat);
		formatDialog.setDefaultTitleRenameFormatString(defaultTitleRenameFormat);
		
		//formatDialog.setModal(true);
		formatDialog.setVisible(true);
		
		/*
		if(formatDialog.getExitCode() == OkDialog.EXIT_OK){
			
			//FileRenamer renamer = Application.getInstance().getRenamer();
			
			String newRenameFormat = formatDialog.getRenameFormatString();
			String newDefaultTitleRenameFormat = formatDialog.getDefaultTitleRenameFormatString();
			
			if(newRenameFormat != null) renameFormatModel.setRenameFormat(newRenameFormat); // TODO: turn this into a model so the list cell renderer can listen to changes to it
			if(newDefaultTitleRenameFormat != null) renameFormatModel.setDefaultTitleRenameFormat(newDefaultTitleRenameFormat);
			
		}*/
		
	}
	
}
