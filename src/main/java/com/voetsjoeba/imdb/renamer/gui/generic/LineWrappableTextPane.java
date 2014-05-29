package com.voetsjoeba.imdb.renamer.gui.generic;

import javax.swing.JTextPane;

/**
 * Same as a regular {@link JTextPane}, except that this version can enable/disable line wrapping.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class LineWrappableTextPane extends JTextPane {
	
	protected boolean lineWrappingEnabled;
	
	public void setLineWrappingEnabled(boolean lineWrappingEnabled) {
		this.lineWrappingEnabled = lineWrappingEnabled;
		repaint();
	}
	
	public boolean getLineWrappingEnabled(){
		return lineWrappingEnabled;
	}
	
	@Override
	public boolean getScrollableTracksViewportWidth() {
		if(lineWrappingEnabled){
			return super.getScrollableTracksViewportWidth();
		} else {
			return false;
		}
	}
	
}
