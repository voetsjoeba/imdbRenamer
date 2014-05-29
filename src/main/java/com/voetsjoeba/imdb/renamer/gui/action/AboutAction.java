package com.voetsjoeba.imdb.renamer.gui.action;

import java.awt.event.ActionEvent;

import com.voetsjoeba.imdb.renamer.gui.Images;
import com.voetsjoeba.imdb.renamer.gui.action.generic.ApplicationAction;
import com.voetsjoeba.imdb.renamer.gui.panel.AboutDialog;

/**
 * Opens the "About..." dialog.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class AboutAction extends ApplicationAction {
	
	private static AboutDialog aboutDialog;
	
	public AboutAction(){
		super("About...", Images.getAboutIcon());
		setTooltip("Displays the About dialog.");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AboutDialog aboutDialog = getAboutDialog();
		aboutDialog.setVisible(true);
	}
	
	protected static AboutDialog getAboutDialog(){
		
		if(aboutDialog == null) aboutDialog = new AboutDialog();
		return aboutDialog;
		
	}
	
}
