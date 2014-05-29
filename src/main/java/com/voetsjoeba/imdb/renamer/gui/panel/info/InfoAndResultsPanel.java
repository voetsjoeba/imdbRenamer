package com.voetsjoeba.imdb.renamer.gui.panel.info;

import java.awt.GridLayout;

import javax.swing.JPanel;

import com.voetsjoeba.imdb.renamer.gui.panel.search.SearchResultsPanel;

/**
 * Joins the info panel and the search results panel side by side.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class InfoAndResultsPanel extends JPanel {
	
	protected InfoPanel infoPanel;
	protected SearchResultsPanel resultsPanel;
	
	public InfoAndResultsPanel(){
		initComponents();
		initLayout();
		initActions();
	}
	
	private void initComponents() {
		infoPanel = new InfoPanel();
		resultsPanel = new SearchResultsPanel();
	}
	
	private void initLayout() {
		
		setLayout(new GridLayout(1, 2));
		add(infoPanel);
		add(resultsPanel);
		
	}
	
	private void initActions() {
		
	}
	
}
