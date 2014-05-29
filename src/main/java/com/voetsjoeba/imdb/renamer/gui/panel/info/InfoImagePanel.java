package com.voetsjoeba.imdb.renamer.gui.panel.info;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.util.BrowserLauncher;
import com.voetsjoeba.imdb.renamer.util.BrowserLauncherException;

/**
 * Displays the currently loaded {@link Title}'s thumbnail image, if any.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class InfoImagePanel extends JPanel {
	
	private JLabel imageLabel;
	
	public InfoImagePanel(){
		initComponents();
		initLayout();
		initActions();
	}
	
	private void initComponents(){
		imageLabel = new JLabel();
		imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	private void initLayout(){
		
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		setLayout(flowLayout);
		add(imageLabel);
		
	}
	
	private void initActions(){
		
	}
	
	public void setTitleThumbnail(final Title title){
		
		imageLabel.setIcon(null);
		for(MouseListener mouseListener : imageLabel.getMouseListeners()) imageLabel.removeMouseListener(mouseListener);
		
		if(title == null) return;
		
		BufferedImage thumbnail = title.getThumbnail();
		if(thumbnail == null) return;
		
		Image resizedThumbnail = thumbnail.getScaledInstance(-1, 180, Image.SCALE_SMOOTH);
		//BufferedImage thumbnailBuffered = new BufferedImage(thumbnail.getWidth(), thumbnail.getHeight(), BufferedImage.TYPE_INT_RGB);
		ImageIcon thumbnailImageIcon = new ImageIcon(resizedThumbnail);
		
		imageLabel.setIcon(thumbnailImageIcon);
		imageLabel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					BrowserLauncher.openURL(title.getUrl());
				}
				catch(BrowserLauncherException blex) {
					Application.getInstance().error(blex.getMessage());
				}
				
			}
			
		});
		
	}
	
}
