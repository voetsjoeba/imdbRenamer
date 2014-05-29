package com.voetsjoeba.imdb.renamer.gui;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.voetsjoeba.imdb.domain.api.Episode;
import com.voetsjoeba.imdb.domain.api.Season;

/**
 * Component responsible for rendering items in an {@link EpisodeTree}.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class EpisodeTreeCellRenderer extends DefaultTreeCellRenderer {
	
	public EpisodeTreeCellRenderer() {
		
		super();
		
		setLeafIcon(Images.getTreeNoneIcon());
		setOpenIcon(null);
		setClosedIcon(null);
		
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		
		//setText(text);
		/*if(value instanceof Season){
			setText("Season " + ((Season) value).getNumber());
		}*/
		if(value instanceof DefaultMutableTreeNode){
			
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
			Object userObject = treeNode.getUserObject();
			
			if(userObject instanceof Season){
				Season season = (Season) userObject;
				setText("Season " + season.getNumber());
			}
			
			if(userObject instanceof Episode){
				Episode episode = (Episode) userObject;
				setText("Episode " + episode.getNumber() + ": " + episode.getTitle());
			}
			
		}
		
		return this;
		
	}
	
}
