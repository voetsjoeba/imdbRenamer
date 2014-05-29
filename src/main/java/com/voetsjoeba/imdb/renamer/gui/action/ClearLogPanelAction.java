package com.voetsjoeba.imdb.renamer.gui.action;

import java.awt.event.ActionEvent;

import com.voetsjoeba.imdb.renamer.gui.Images;
import com.voetsjoeba.imdb.renamer.gui.action.generic.ApplicationAction;
import com.voetsjoeba.imdb.renamer.gui.panel.LogPanel;

/**
 * Clears the log panel.
 * 
 * @see LogPanel
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class ClearLogPanelAction extends ApplicationAction {
	
	protected LogPanel logPanel;
	
	public ClearLogPanelAction(LogPanel logPanel){
		
		super("Clear", Images.getClearLogIcon());
		
		this.logPanel = logPanel;
		
		setTooltip("Clears the log panel.");
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		logPanel.clear();
	}
	
}
