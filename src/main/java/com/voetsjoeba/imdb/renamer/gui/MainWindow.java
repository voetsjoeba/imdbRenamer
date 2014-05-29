package com.voetsjoeba.imdb.renamer.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.gui.panel.files.FileListPanel;
import com.voetsjoeba.imdb.renamer.gui.panel.info.InfoAndResultsPanel;
import com.voetsjoeba.imdb.renamer.gui.panel.search.SearchPanel;

/**
 * Main application window.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame implements WindowListener {
	
	protected MainMenu mainMenu;
	
	protected SearchPanel searchPanel;
	protected InfoAndResultsPanel infoAndChoosePanel;
	protected FileListPanel fileListPanel;
	
	//protected TrayIcon trayIcon;
	protected JPopupMenu trayMenu;
	private JPanel renamePanel;
	private JTabbedPane tabbedPane;
	private JPanel databasePanel;
	//protected LogPanel logPanel;
	
	public MainWindow() {
		initComponents();
		initLayout();
		initActions();
	}
	
	protected void initComponents() {
		
		//logPanel = new LogPanel("main");
		mainMenu = new MainMenu();
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		renamePanel = new JPanel();
		searchPanel = new SearchPanel();
		fileListPanel = new FileListPanel();
		infoAndChoosePanel = new InfoAndResultsPanel();
		
		//trayMenu = new JPopupMenu();
		//trayMenu.add(new JMenuItem("Test Item"));

		//trayIcon = new TrayIcon(Icons.getApplicationIcon(), "Tray Test", trayMenu);
		//SystemTray.getDefaultSystemTray().addTrayIcon(trayIcon);
		
	}
	
	protected void initLayout(){
		
		//======== this ========
		setTitle(Application.getInstance().getName());
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setIconImage((Images.getApplicationIcon()).getImage());
		getToolkit().setDynamicLayout(true); // dynamically repaint while resizing
		setLocationRelativeTo(getOwner());
		setJMenuBar(mainMenu);
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(tabbedPane);
		
		tabbedPane.addTab("Rename", null, renamePanel, "Perform a rename operation");
		GridBagLayout gbl_renamePanel = new GridBagLayout();
		gbl_renamePanel.columnWidths = new int[]{0, 0};
		gbl_renamePanel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_renamePanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_renamePanel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		renamePanel.setLayout(gbl_renamePanel);
		
		
		GridBagConstraints gbc_searchPanel = new GridBagConstraints();
		gbc_searchPanel.fill = GridBagConstraints.BOTH;
		gbc_searchPanel.insets = new Insets(0, 0, 5, 0);
		gbc_searchPanel.gridx = 0;
		gbc_searchPanel.gridy = 0;
		renamePanel.add(searchPanel, gbc_searchPanel);
		
		getRootPane().setDefaultButton(searchPanel.getSearchButton());
		
		GridBagConstraints gbc_infoAndChoosePanel = new GridBagConstraints();
		gbc_infoAndChoosePanel.fill = GridBagConstraints.BOTH;
		gbc_infoAndChoosePanel.insets = new Insets(0, 0, 5, 0);
		gbc_infoAndChoosePanel.gridx = 0;
		gbc_infoAndChoosePanel.gridy = 1;
		renamePanel.add(infoAndChoosePanel, gbc_infoAndChoosePanel);
		
		GridBagConstraints gbc_fileListPanel = new GridBagConstraints();
		gbc_fileListPanel.fill = GridBagConstraints.BOTH;
		gbc_fileListPanel.gridx = 0;
		gbc_fileListPanel.gridy = 2;
		renamePanel.add(fileListPanel, gbc_fileListPanel);
		
		databasePanel = new JPanel();
		tabbedPane.addTab("Database", null, databasePanel, "View the database of known titles and renaming rules");
		
		/*contentPane.add(logPanel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));*/
		
		setSize(1160, 760);
		setLocationRelativeTo(getOwner());
		
	}
	
	protected void initActions(){
		addWindowListener(this);
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		Application.getInstance().exit();
	}
	
	@Override public void windowActivated(WindowEvent e) {}
	@Override public void windowClosed(WindowEvent e) {}
	@Override public void windowDeactivated(WindowEvent e) {}
	@Override public void windowDeiconified(WindowEvent e) {}
	@Override public void windowIconified(WindowEvent e) {}
	@Override public void windowOpened(WindowEvent e) {}
	
}
