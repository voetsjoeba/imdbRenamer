package com.voetsjoeba.imdb.renamer.gui.generic;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voetsjoeba.imdb.renamer.util.BrowserLaunchAction;

/**
 * Simulates a hyperlinked label using a JButton and some fancy makeup. Opens the user's browser to the specified URL when clicked.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class HyperlinkLabel extends JButton {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(HyperlinkLabel.class);
	
	public enum UnderlineBehaviour {
		ALWAYS,
		ROLLOVER
	}
	
	protected String url;
	protected String text;
	protected Color foregroundColor;
	protected Color hoverForegroundColor;
	
	protected boolean textUnderlined;
	protected final UnderlineBehaviour underlineBehaviour;
	
	public HyperlinkLabel(){
		this("", "");
	}
	
	public HyperlinkLabel(String url){
		this(url, url);
	}
	
	public HyperlinkLabel(String text, String url) {
		this(text, url, new BrowserLaunchAction());
	}
	
	public HyperlinkLabel(String text, String url, ActionListener actionListener) {
		this(text, url, actionListener, UnderlineBehaviour.ROLLOVER, Color.BLUE, Color.BLUE);
	}
	
	public HyperlinkLabel(String text, String url, ActionListener actionListener, UnderlineBehaviour ulBehaviour, Color fgColor, Color hoverFgColor){
		
		if(fgColor == null) throw new IllegalArgumentException("Foreground color must not be null");
		if(ulBehaviour == null) throw new IllegalArgumentException("Underline behaviour must not be null");
		
		setHorizontalAlignment(SwingConstants.LEFT);
		setBorderPainted(false);
		setOpaque(false);
		setBorder(null);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setIconTextGap(0);
		setContentAreaFilled(false);
		setFocusPainted(false);
		
		foregroundColor = fgColor;
		hoverForegroundColor = hoverFgColor;
		underlineBehaviour = ulBehaviour;
		
		setForeground(fgColor);
		setText(text, url);
		
		switch(underlineBehaviour){
			
			case ALWAYS:
				textUnderlined = true;
				break;
				
			case ROLLOVER:
				textUnderlined = false;
				break;
				
			default:
				throw new RuntimeException("Unexpected underline behaviour " + underlineBehaviour);
		}
		
		if(underlineBehaviour == UnderlineBehaviour.ROLLOVER || !fgColor.equals(hoverFgColor)){
			addMouseListener(new RollOverListener());
		}
		
		addActionListener(actionListener);
		
	}
	
	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		
		// paint a basic underline
		if(textUnderlined){
			Rectangle r = g.getClipBounds();
			g.drawLine(0, r.height-2, r.width, r.height-2);
		}
		
	}
	
	protected void setTextUnderlined(boolean textUnderlined){
		this.textUnderlined = textUnderlined;
		repaint();
	}
	
	@Override
	public String getText(){
		return text;
	}
	
	public String getUrl(){
		return url;
	}
	
	@Override
	public void setText(String url){
		setText(url, url);
	}
	
	public void setText(String text, final String url){
		setText(text, url, url);
	}
	
	public void setText(String text, final String url, final String tooltipText){
		
		this.text = text;
		this.url = url;
		
		if(!StringUtils.isEmpty(text)){
			
			super.setText(text);
			if(!StringUtils.isEmpty(url)) setToolTipText(tooltipText);
			
		} else {
			
			super.setText("");
			
		}
		
	}
	
	public UnderlineBehaviour getUnderlineBehaviour() {
		return underlineBehaviour;
	}
	
	private class RollOverListener extends MouseAdapter {
		
		@Override
		public void mouseEntered(MouseEvent e) {
			setForeground(hoverForegroundColor);
			if(underlineBehaviour == UnderlineBehaviour.ROLLOVER) setTextUnderlined(true);
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			setForeground(foregroundColor);
			if(underlineBehaviour == UnderlineBehaviour.ROLLOVER) setTextUnderlined(false);
		}
		
	}
	
}
