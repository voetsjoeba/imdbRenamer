package com.voetsjoeba.imdb.renamer.gui.panel.registry;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.voetsjoeba.imdb.domain.LimitedTitle;
import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.gui.action.LimitedTitleRegistryRemoveAction;

/**
 * List component in the {@link TitleRegistryViewerDialog} dialog.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class TitleRegistryViewerList extends JList implements MouseListener, KeyListener {
	
	protected TitleRegistryViewerListModel listModel;
	protected TitleRegistryViewerItem listCellRenderer;
	
	public TitleRegistryViewerList() {
		
		listModel = new TitleRegistryViewerListModel();
		listCellRenderer = new TitleRegistryViewerItem();
		
		setModel(listModel);
		setCellRenderer(listCellRenderer); // TODO: for some reason this causes a crash when opening the list viewer
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		addMouseListener(this);
		addKeyListener(this);
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		boolean isLeftMouseButton = SwingUtilities.isLeftMouseButton(e);
		boolean isRightMouseButton = SwingUtilities.isRightMouseButton(e);
		boolean isDoubleClick = (e.getClickCount() >= 2);
		
		int clickedIndex = TitleRegistryViewerList.this.locationToIndex(e.getPoint()); // index of item where mouse was clicked
		int[] selectedIndices = TitleRegistryViewerList.this.getSelectedIndices(); // list of currently selected items
		
		// check if the mouse was clicked at at least one of the currently selected items
		Arrays.sort(selectedIndices);
		boolean clickedSelectedItem = (Arrays.binarySearch(selectedIndices, clickedIndex) >= 0);
		
		if(selectedIndices.length > 0 && isRightMouseButton && clickedSelectedItem){
			
			Object[] objectsToRemove = TitleRegistryViewerList.this.getSelectedValues();
			List<LimitedTitle> titlesToRemove = new ArrayList<LimitedTitle>(objectsToRemove.length);
			
			for(Object objectToRemove : objectsToRemove){
				titlesToRemove.add((LimitedTitle) objectToRemove);
			}
			
			Action removeItemsAction = new LimitedTitleRegistryRemoveAction(titlesToRemove);
			
			JPopupMenu popupMenu = new JPopupMenu();
			popupMenu.add(removeItemsAction);
			
			popupMenu.show(TitleRegistryViewerList.this, e.getX(), e.getY());
			
		}
		
		if(isLeftMouseButton && isDoubleClick){
			// TODO: do sth here, possibly
		}
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_DELETE){
			
			Object[] selectedValues = getSelectedValues();
			for(Object selectedValue : selectedValues){
				
				if(selectedValue instanceof Title){
					
					LimitedTitle selectedTitle = (LimitedTitle) selectedValue;
					
					LimitedTitleRegistryRemoveAction removeFilesAction = new LimitedTitleRegistryRemoveAction(Arrays.asList(selectedTitle));
					removeFilesAction.actionPerformed(null);
					
				}
				
			}
			
		}
		
	}
	
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	
	@Override public void keyReleased(KeyEvent e) {}
	@Override public void keyTyped(KeyEvent e) {}
	
}
