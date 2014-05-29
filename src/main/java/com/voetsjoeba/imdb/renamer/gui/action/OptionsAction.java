package com.voetsjoeba.imdb.renamer.gui.action;

import java.awt.event.ActionEvent;

import com.voetsjoeba.imdb.renamer.gui.Images;
import com.voetsjoeba.imdb.renamer.gui.action.generic.ApplicationAction;
import com.voetsjoeba.imdb.renamer.gui.panel.OptionsDialog;

/**
 * Opens the application settings.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class OptionsAction extends ApplicationAction {
	
	public OptionsAction(){
		super("Preferences...", Images.getOptionsIcon());
		setTooltip("Displays the Preferences dialog.");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		OptionsDialog options = new OptionsDialog();
		options.setModal(true);
		options.setVisible(true);
		
	}
	
}
