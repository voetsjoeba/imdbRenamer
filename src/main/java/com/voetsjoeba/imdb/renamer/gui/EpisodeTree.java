package com.voetsjoeba.imdb.renamer.gui;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import com.voetsjoeba.imdb.domain.api.Episode;
import com.voetsjoeba.imdb.domain.api.Season;
import com.voetsjoeba.imdb.domain.api.Series;
import com.voetsjoeba.imdb.renamer.gui.generic.ApplicationTree;

/**
 * A tree component showing the seasons and episodes in a {@link Series}.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class EpisodeTree extends ApplicationTree {
	
	protected Series series;
	
	protected TreeModel treeModel;
	protected EpisodeTreeCellRenderer cellRenderer;
	
	public EpisodeTree(Series series) {
		
		this.series = series;
		
		initComponents();
		initLayout();
		initActions();
		
	}
	
	private void initComponents() {
		
		cellRenderer = new EpisodeTreeCellRenderer();
		
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
		for(Season season : series.getSeasons()){
			
			DefaultMutableTreeNode seasonNode = new DefaultMutableTreeNode();
			seasonNode.setUserObject(season);
			
			for(Episode episode : season.getEpisodes()){
				
				DefaultMutableTreeNode episodeNode = new DefaultMutableTreeNode();
				episodeNode.setUserObject(episode);
				
				seasonNode.add(episodeNode);
				
			}
			
			rootNode.add(seasonNode);
			
		}
		
		treeModel = new DefaultTreeModel(rootNode);
		setModel(treeModel);
		
		setRootVisible(false);
		setShowsRootHandles(true);
		setCellRenderer(cellRenderer);
		expandAllNodes(true);
		
	}
	
	private void initLayout() {
		
	}
	
	private void initActions() {
		
	}
	
}
