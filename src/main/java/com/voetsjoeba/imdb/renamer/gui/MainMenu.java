package com.voetsjoeba.imdb.renamer.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.gui.action.generic.ActionSingleton;
import com.voetsjoeba.imdb.renamer.gui.panel.ExceptionTestPanel;

/**
 * Main application menu.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class MainMenu extends JMenuBar {
	
	private JMenu fileMenu;
	private JMenuItem openFilesMenuItem;
	private JMenuItem openFolderMenuItem;
	private JMenuItem clearMenuItem;
	private JMenuItem performRenameMenuItem;
	private JMenuItem removeUnaffectedMenuItem;
	private JMenuItem exitMenuItem;
	
	private JMenu toolsMenu;
	private JMenuItem viewTitleRegistryMenuItem;
	private JMenuItem renameFormatsMenuItem;
	private JMenuItem settingsMenuItem;
	
	private JMenu helpMenu;
	private JMenuItem aboutMenuItem;
	
	public MainMenu() {
		initComponents();
	}
	
	private void initComponents() {
		
		fileMenu = new JMenu();
		// -----------------------------------
		openFilesMenuItem = new JMenuItem(ActionSingleton.getOpenFilesAction());
		openFolderMenuItem = new JMenuItem(ActionSingleton.getOpenFolderAction());
		// -----------------------------------
		clearMenuItem = new JMenuItem(ActionSingleton.getClearFilesAction());
		performRenameMenuItem = new JMenuItem(ActionSingleton.getPerformRenameAction());
		removeUnaffectedMenuItem = new JMenuItem(ActionSingleton.getRemoveUnaffectedFilesAction());
		// -----------------------------------
		exitMenuItem = new JMenuItem(ActionSingleton.getExitAction());
		
		
		toolsMenu = new JMenu();
		// -----------------------------------
		viewTitleRegistryMenuItem = new JMenuItem(ActionSingleton.getViewTitleRegistryAction());
		renameFormatsMenuItem = new JMenuItem(ActionSingleton.getRenameFormatsDialogAction());
		settingsMenuItem = new JMenuItem(ActionSingleton.getSettingsAction());
		
		
		helpMenu = new JMenu();
		// -----------------------------------
		aboutMenuItem = new JMenuItem(ActionSingleton.getAboutAction());
		
		//======== this ========
		
		//======== fileMenu ========
		{
			fileMenu.setText("File");
			
			//---- openMenuItem ----
			//openMenuItem.setText("Open...");
			fileMenu.add(openFilesMenuItem);
			fileMenu.add(openFolderMenuItem);
			// TODO: remove me from production
			fileMenu.add(new AbstractAction("Test Exceptions"){
				@Override public void actionPerformed(ActionEvent e) {
					JDialog foo = new JDialog(Application.getInstance().getMainWindow());
					foo.getContentPane().add(new ExceptionTestPanel());
					foo.pack();
					foo.setLocationRelativeTo(null);
					foo.setVisible(true);
				}
			});
			fileMenu.addSeparator();
			
			//---- performRenameMenuItem ----
			fileMenu.add(clearMenuItem);
			fileMenu.add(performRenameMenuItem);
			fileMenu.add(removeUnaffectedMenuItem);
			fileMenu.addSeparator();
			
			//---- exitMenuItem ----
			//exitMenuItem.setText("Exit");
			fileMenu.add(exitMenuItem);
		}
		add(fileMenu);
		
		//======== toolsMenu ========
		{
			toolsMenu.setText("Tools");
			
			//---- settingsMenuItem ----
			//settingsMenuItem.setText("Settings...");
			toolsMenu.add(viewTitleRegistryMenuItem);
			toolsMenu.add(renameFormatsMenuItem);
			toolsMenu.add(settingsMenuItem);
		}
		add(toolsMenu);
		
		//======== helpMenu ========
		{
			helpMenu.setText("Help");
			
			//---- aboutMenuItem ----
			helpMenu.add(aboutMenuItem);
		}
		add(helpMenu);
		
	}
	
}
