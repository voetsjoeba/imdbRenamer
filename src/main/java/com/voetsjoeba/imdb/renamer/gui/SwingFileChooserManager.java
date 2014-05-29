package com.voetsjoeba.imdb.renamer.gui;

import java.io.File;

import javax.swing.JFileChooser;

import com.voetsjoeba.imdb.renamer.Application;

/**
 * Holds and manages file and folder choosers. Persists the folder path most recently used to select files from 
 * for improved usability.
 * 
 * @author Jeroen De Ridder
 */
public class SwingFileChooserManager implements FileChooserManager {
	
	private JFileChooser fileChooser;
	private JFileChooser folderChooser;
	
	public SwingFileChooserManager(){
		init();
	}
	
	protected void init(){
		
		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select files...");
		fileChooser.setMultiSelectionEnabled(true);
		
		folderChooser = new JFileChooser();
		folderChooser.setDialogTitle("Select folder...");
		folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
	}
	
	@Override
	public File[] selectFiles(){
		
		int dialogResult = fileChooser.showOpenDialog(Application.getInstance().getMainWindow());
		if(dialogResult != JFileChooser.APPROVE_OPTION) return new File[]{};
		
		return fileChooser.getSelectedFiles();
		
	}
	
	@Override
	public File selectFolder(){
		
		int dialogResult = folderChooser.showOpenDialog(Application.getInstance().getMainWindow());
		if(dialogResult != JFileChooser.APPROVE_OPTION) return null;
		
		return folderChooser.getSelectedFile();
		
	}
	
}
