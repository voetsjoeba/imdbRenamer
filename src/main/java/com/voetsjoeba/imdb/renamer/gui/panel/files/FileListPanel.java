package com.voetsjoeba.imdb.renamer.gui.panel.files;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.voetsjoeba.imdb.renamer.gui.panel.files.table.FileListTable;

@SuppressWarnings("serial")
public class FileListPanel extends JPanel {
	
	private FileListToolBarPanel toolBarPanel;
	private FileListTable fileListTable;
	private JScrollPane fileListTableScrollPane;
	
	public FileListPanel(){
		initComponents();
		initLayout();
		initActions();
	}
	
	protected void initComponents() {
		toolBarPanel = new FileListToolBarPanel();
		
	}
	
	protected void initLayout() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0};
		setLayout(gridBagLayout);
		((GridBagLayout) getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout) getLayout()).rowHeights = new int[] {0, 0, 0};
		((GridBagLayout) getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout) getLayout()).rowWeights = new double[] {0.0, 1.0, 1.0E-4};
		
		//======== fileToolBar ========
		add(toolBarPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));
		
		fileListTableScrollPane = new JScrollPane();
		GridBagConstraints gbc_fileListTableScrollPane = new GridBagConstraints();
		gbc_fileListTableScrollPane.fill = GridBagConstraints.BOTH;
		gbc_fileListTableScrollPane.gridx = 0;
		gbc_fileListTableScrollPane.gridy = 1;
		add(fileListTableScrollPane, gbc_fileListTableScrollPane);
		
		fileListTable = new FileListTable();
		fileListTable.setFillsViewportHeight(true);
		fileListTable.setAlignmentX(Component.LEFT_ALIGNMENT);
		fileListTable.setAlignmentY(Component.TOP_ALIGNMENT);
		fileListTableScrollPane.setViewportView(fileListTable);
		
	}
	
	protected void initActions() {
		
	}
	
}
