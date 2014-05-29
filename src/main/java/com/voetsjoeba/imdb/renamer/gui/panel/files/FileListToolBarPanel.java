package com.voetsjoeba.imdb.renamer.gui.panel.files;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.voetsjoeba.imdb.renamer.gui.action.generic.ActionSingleton;

@SuppressWarnings("serial")
public class FileListToolBarPanel extends JPanel {
	
	protected FileListToolBar toolBar;
	protected JPanel rightPanel;
	protected JButton renameFormatButton;
	
	public FileListToolBarPanel(){
		
		initComponents();
		initLayout();
		initActions();
		
	}
	
	protected void initComponents(){
		
		toolBar = new FileListToolBar();
		rightPanel = new JPanel();
		
		renameFormatButton = new JButton(ActionSingleton.getRenameFormatsDialogAction());
		
	}
	
	protected void initLayout(){
		
		setLayout(new BorderLayout());
		
		rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		rightPanel.add(renameFormatButton);
		
		add(toolBar, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);
		
	}
	
	protected void initActions(){
		
	}
	
}
