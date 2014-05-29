package com.voetsjoeba.imdb.renamer.gui.action.generic;

import javax.swing.Icon;


/**
 * Generic superclass for actions that can enable/disable themselves based on some condition. Publishes an {@link #updateEnabledStatus()}
 * method to enable components to update the action to its initial enabled/disabled state.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public abstract class EnabledStatusAction extends ApplicationAction {
	
	public EnabledStatusAction(String name, Icon icon) {
		super(name, icon);
	}
	
	public EnabledStatusAction(String name) {
		super(name);
	}
	
	public abstract void updateEnabledStatus();
	
}
