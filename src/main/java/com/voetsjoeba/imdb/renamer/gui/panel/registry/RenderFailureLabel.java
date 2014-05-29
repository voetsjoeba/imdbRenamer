package com.voetsjoeba.imdb.renamer.gui.panel.registry;

import javax.swing.JLabel;

import com.voetsjoeba.imdb.renamer.gui.Images;

/**
 * Indicates that a particular list entry could not be rendered.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class RenderFailureLabel extends JLabel {
	
	public RenderFailureLabel(String msg) {
		super(msg, Images.getErrorIcon(), JLabel.LEFT);
	}
	
}
