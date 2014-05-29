package com.voetsjoeba.imdb.renamer.gui.action;

import java.awt.event.ActionEvent;

import com.voetsjoeba.imdb.renamer.gui.action.generic.ApplicationAction;
import com.voetsjoeba.imdb.renamer.gui.panel.registry.TitleRegistryViewerDialog;

/**
 * Presents the view cache dialog when executed.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class ViewTitleRegistryAction extends ApplicationAction {
	
	public ViewTitleRegistryAction(){
		super("View Title Registry"); // TODO: i18n
		setTooltip("Displays the View Title Registry dialog.");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		TitleRegistryViewerDialog titleRegistryViewerDialog = new TitleRegistryViewerDialog();
		titleRegistryViewerDialog.setVisible(true);
	}
	
}
