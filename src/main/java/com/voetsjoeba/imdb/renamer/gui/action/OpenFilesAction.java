package com.voetsjoeba.imdb.renamer.gui.action;

import java.awt.event.ActionEvent;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.gui.Images;
import com.voetsjoeba.imdb.renamer.gui.action.generic.ApplicationAction;

/**
 * Displays a file chooser to let the user pick one or multiple files to load.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class OpenFilesAction extends ApplicationAction {
	
	public OpenFilesAction() {
		super("Open...", Images.getOpenFilesIcon());
		setTooltip("Load one or more files.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Application.getInstance().openFiles();
	}
	
}
