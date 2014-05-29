package com.voetsjoeba.imdb.renamer.gui.panel.registry;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.voetsjoeba.imdb.renamer.gui.generic.OkDialog;

/**
 * Allows the user to view and manage the cache.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class TitleRegistryViewerDialog extends OkDialog {
	
	protected TitleRegistryViewerList cacheViewerList;
	protected JScrollPane cacheViewerListScrollPane;
	
	public TitleRegistryViewerDialog(){
		
		initComponents();
		initLayout();
		initActions();
		
	}
	
	protected void initComponents() {
		
		setTitle("Title Registry");
		
		cacheViewerList = new TitleRegistryViewerList();
		cacheViewerList.setAlignmentY(Component.TOP_ALIGNMENT);
		cacheViewerList.setAlignmentX(Component.LEFT_ALIGNMENT);
		cacheViewerListScrollPane = new JScrollPane();
		cacheViewerListScrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
		cacheViewerListScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		
	}
	
	protected void initLayout() {
		
		JPanel contentPanel = getChildContentPanel();
		
		cacheViewerListScrollPane.setViewportView(cacheViewerList);
		
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(cacheViewerListScrollPane, BorderLayout.CENTER);
		
	}
	
	protected void initActions() {
		
	}
	
}
