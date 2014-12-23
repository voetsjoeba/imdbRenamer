package com.voetsjoeba.imdb.renamer.gui.panel.files.list;

import java.awt.Component;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.event.FileAddedEvent;
import com.voetsjoeba.imdb.renamer.event.FileListListener;
import com.voetsjoeba.imdb.renamer.event.FileRemovedEvent;
import com.voetsjoeba.imdb.renamer.event.FilesClearedEvent;
import com.voetsjoeba.imdb.renamer.model.TitleModel;

@SuppressWarnings("serial")
public class FileListCellRenderer extends JPanel implements ListCellRenderer<File>, FileListListener {
	
	/*private JPanel entryControlsPanel;
	private JButton removeButton;*/
	
	protected TitleModel titleModel;
	protected Map<File, FileListItem> fileListItems;
	
	public FileListCellRenderer() {
		titleModel = Application.getInstance().getTitleModel();
		fileListItems = new HashMap<File, FileListItem>();
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends File> list, File file, int index, boolean isSelected, boolean cellHasFocus) {
		
		if(file == null) return null;
		
		FileListItem fileListItem = fileListItems.get(file); // get previously mapped component, if any
		if(!fileListItems.containsKey(file)){
			
			fileListItem = new FileListItem(file); // if there is no previously mapped component, the fetched value above is invalid and we should create a new one instead
			//titleModel.addTitleSelectionListener(fileListItem);
			
			fileListItems.put(file, fileListItem);
			
		}
		
		fileListItem.updateState(list, file, index, isSelected, cellHasFocus);
		return fileListItem;
		
	}
	
	@Override
	public void fileAdded(FileAddedEvent e) {
		// no action needed, handled by getListCellRendererComponent
	}
	
	@Override
	public void fileRemoved(FileRemovedEvent e) {
		
		File removedFile = e.getObject();
		//FileListItem fileListItem = fileListItems.get(removedFile);
		
		// this is needed so we don't leak memory by holding FileListItems in the mapping for files or in the event listener list
		// of the title selection model that have long been deleted
		fileListItems.remove(removedFile);
		//titleModel.removeTitleSelectionListener(fileListItem);
		
	}
	
	@Override
	public void filesCleared(FilesClearedEvent e) {
		
		// see fileRemoved
		
		/*for(FileListItem fileListItem : fileListItems.values()){
			titleModel.removeTitleSelectionListener(fileListItem);
		}*/
		
		fileListItems.clear();
		
	}
	
	/*public void titleSelected(TitleSelectedEvent e) {
		updateTitleSelectedStatus(null);
		repaint();
	}*/
	
}
