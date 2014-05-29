package com.voetsjoeba.imdb.renamer.gui.action.generic;

import java.awt.event.ActionEvent;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.gui.Images;

/**
 * Exits the application.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class ExitAction extends ApplicationAction {

	public ExitAction(){
		super("Exit", Images.getBlankIcon());
		setTooltip("Exits the application.");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Application.getInstance().exit();
	}
	
}
