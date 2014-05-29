package com.voetsjoeba.imdb.renamer.gui.action;

import java.awt.event.ActionEvent;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.gui.Images;
import com.voetsjoeba.imdb.renamer.gui.action.generic.ApplicationAction;

/**
 * Displays a file chooser to pick a directory from which to add files. Only files directly under the directory are considered,
 * i.e. files further down the folder hierarchy are not added, only files that are direct children of the chosen folder.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class OpenFolderAction extends ApplicationAction {
	
	public OpenFolderAction(){
		super("Open Folder...", Images.getOpenFolderIcon());
		setTooltip("Load files from a directory. Only files directly under the chosen directory are loaded.");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Application.getInstance().openFolder();
	}
	
}
