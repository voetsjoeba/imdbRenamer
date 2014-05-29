package com.voetsjoeba.imdb.renamer.gui;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.ImageIcon;

/**
 * A blank Swing icon.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class BlankIcon extends ImageIcon {
	
	private int width;
	private int height;
	
	public BlankIcon() {
		this(16, 16);
	}
	
	public BlankIcon(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public int getIconWidth() {
		return width;
	}
	
	@Override
	public int getIconHeight() {
		return height;
	}
	
	/**
	 * Empty implementation; does not draw anything.
	 */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		return;
	}
	
}
