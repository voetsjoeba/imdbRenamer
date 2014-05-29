package com.voetsjoeba.imdb.renamer.gui.panel;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voetsjoeba.imdb.domain.api.Episode;
import com.voetsjoeba.imdb.domain.api.Series;
import com.voetsjoeba.imdb.renamer.gui.EpisodeTree;
import com.voetsjoeba.imdb.renamer.gui.generic.OkDialog;

/**
 * Dialog that allows for the user to pick one episode from the list of episodes in a {@link Series}.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class SeriesMappingDialog extends OkDialog implements MouseListener {
	
	private static final Logger log = LoggerFactory.getLogger(SeriesMappingDialog.class);
	
	protected Series series;
	
	/*protected JComboBox episodesComboBox;
	protected DefaultComboBoxModel episodesComboBoxModel;*/
	
	protected EpisodeTree episodeTree;
	protected JScrollPane episodeTreeScrollPane;
	
	protected Object selected;
	
	public SeriesMappingDialog(Series series){
		
		super();
		this.series = series;
		
		initComponents();
		initLayout();
		initActions();
		
	}
	
	private void initComponents() {
		
		setTitle("Choose season/episode number:");
		
		episodeTree = new EpisodeTree(series);
		
		episodeTreeScrollPane = new JScrollPane();
		episodeTreeScrollPane.setViewportView(episodeTree);
		
	}
	
	private void initLayout() {
		
		JPanel contentPanel = getChildContentPanel();
		
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(episodeTreeScrollPane, BorderLayout.CENTER);
		
	}
	
	private void initActions() {
		
		//episodeTree.addTreeSelectionListener(this);
		episodeTree.addMouseListener(this);
		
	}
	
	public List<Episode> getSelectedItems(){
		
		TreePath[] selectionPaths = episodeTree.getSelectionPaths();
		if(selectionPaths == null) return Collections.emptyList();
		
		List<Episode> selectedItems = new ArrayList<Episode>(selectionPaths.length);
		
		for(TreePath selectionPath : selectionPaths){
			
			Object treeNode = selectionPath.getLastPathComponent();
			
			if(treeNode instanceof DefaultMutableTreeNode){
				
				DefaultMutableTreeNode mutableTreeNode = (DefaultMutableTreeNode) treeNode;
				Object userObject = mutableTreeNode.getUserObject();
				
				if(userObject instanceof Episode){
					selectedItems.add((Episode) userObject);
				} else {
					log.warn("Found non-episode tree node user object in series episode mapping dialog");
				}
				
			}
			
		}
		
		return selectedItems;
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() >= 2){
			close();
		}
		
	}
	
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	
}
