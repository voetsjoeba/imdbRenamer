package com.voetsjoeba.imdb.renamer.gui.action.generic;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * Generic superclass for all Actions in the application. Provides some utility functionality.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public abstract class ApplicationAction extends AbstractAction {
	
	public ApplicationAction(String name, Icon icon) {
		super(name, icon);
	}
	
	public ApplicationAction(String name) {
		super(name);
	}
	
	public void setTooltip(String tooltip){
		putValue(SHORT_DESCRIPTION, tooltip);
	}
	
	public void setName(String name){
		putValue(NAME, name);
	}
	
}
