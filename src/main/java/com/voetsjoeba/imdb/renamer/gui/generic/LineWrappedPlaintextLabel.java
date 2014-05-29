package com.voetsjoeba.imdb.renamer.gui.generic;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("serial")
public class LineWrappedPlaintextLabel extends JLabel {

	public LineWrappedPlaintextLabel() {
		super();
	}

	public LineWrappedPlaintextLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
	}

	public LineWrappedPlaintextLabel(Icon image) {
		super(image);
	}

	public LineWrappedPlaintextLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
	}

	public LineWrappedPlaintextLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
	}

	public LineWrappedPlaintextLabel(String text) {
		super(text);
	}
	
	@Override
	public void setText(String text){
		
		if(!StringUtils.startsWith(text, "<html")){
			super.setText("<html><body>"+text+"</body></html>");
		} else {
			super.setText(text);
		}
		
	}
	
}
