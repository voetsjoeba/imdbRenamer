package com.voetsjoeba.imdb.renamer.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.gui.generic.HyperlinkLabel;

/**
 * Action to launch a browser window when a {@link HyperlinkLabel} is clicked.
 * 
 * @author Jeroen
 */
public class BrowserLaunchAction implements ActionListener {
	
	@Override public void actionPerformed(ActionEvent e) {
		try {
			HyperlinkLabel hyperlinkLabel = (HyperlinkLabel) e.getSource();
			BrowserLauncher.openURL(hyperlinkLabel.getUrl());
		}
		catch(BrowserLauncherException blex){
			Application.getInstance().error("An error occured when attempting to launch a browser window:\n%s", blex.getMessage());
		}
	}
}