package com.voetsjoeba.imdb.renamer.gui.panel.files;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.voetsjoeba.imdb.renamer.gui.action.generic.ActionSingleton;

@SuppressWarnings("serial")
public class FileListToolBar extends JToolBar {
	
	private JButton openFilesButton;
	private JButton openFolderButton;
	private JButton performRenameButton;
	private JButton clearFilesButton;
	private JButton removeUnaffectedFilesButton;
	
	//private PerformRenameAction performRenameAction;
	//private ClearFilesAction clearFilesAction;
	
	public FileListToolBar() {
		initComponents();
		initLayout();
		initActions();
	}
	
	protected void initComponents() {
		
		//clearFilesAction = new ClearFilesAction();
		//performRenameAction = new PerformRenameAction();
		
		openFilesButton = new JButton(ActionSingleton.getOpenFilesAction());
		openFolderButton = new JButton(ActionSingleton.getOpenFolderAction());
		performRenameButton = new JButton(ActionSingleton.getPerformRenameAction());
		clearFilesButton = new JButton(ActionSingleton.getClearFilesAction());
		removeUnaffectedFilesButton = new JButton(ActionSingleton.getRemoveUnaffectedFilesAction());
		
	}
	
	protected void initLayout(){
		
		//======== this ========
		setFloatable(false);
		
		performRenameButton.setMnemonic('P');
		
		add(openFilesButton);
		add(openFolderButton);
		add(performRenameButton);
		add(clearFilesButton);
		add(removeUnaffectedFilesButton);
		
	}
	
	protected void initActions(){
		
		
		
		
		
		//clearFilesAction.updateEnabledStatus();
		//ActionSingleton.getPerformRenameAction().updateEnabledStatus();
		
	}
	
}