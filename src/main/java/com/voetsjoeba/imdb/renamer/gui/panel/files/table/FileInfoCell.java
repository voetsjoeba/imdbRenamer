package com.voetsjoeba.imdb.renamer.gui.panel.files.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.io.FilenameUtils;

/**
 * Generic list/table item rendering two labels stacked on top of eachother, with the top one indicating a file name
 * and the bottom one indicating that file's path.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public abstract class FileInfoCell extends JPanel implements TableCellRenderer, FileListTableConstants {
	
	protected static final Color directoryLabelSelection = Color.LIGHT_GRAY;
	protected static final Color directoryLabelSelectionForeground = Color.decode("0xDDDDDD");
	
	protected JLabel filenameLabel;
	protected JLabel pathLabel;
	
	protected Font regularLabelFont;
	protected Font boldLabelFont;
	
	protected boolean alternateRowBackground = false;
	
	public FileInfoCell() {
		this(false);
	}
	
	public FileInfoCell(boolean alternateRowBackground) {
		
		this.alternateRowBackground = alternateRowBackground;
		
		initComponents();
		initLayout();
		
	}
	
	private void initComponents(){
		filenameLabel = new JLabel("filename");
		pathLabel = new JLabel("/path/to/directory");
		
		regularLabelFont = filenameLabel.getFont();
		boldLabelFont = regularLabelFont.deriveFont(Font.BOLD);
	}
	
	private void initLayout(){
		
		//======== this ========
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{1.0};
		setLayout(gridBagLayout);
		setBorder(new EmptyBorder(2, 2, 2, 2));
		
		setOpaque(true);
		setBackground(Color.WHITE);
		
		GridBagConstraints gbc_filenameLabel = new GridBagConstraints();
		gbc_filenameLabel.insets = new Insets(0, 0, 2, 0);
		gbc_filenameLabel.anchor = GridBagConstraints.WEST;
		gbc_filenameLabel.gridx = 0;
		gbc_filenameLabel.gridy = 0;
		add(filenameLabel, gbc_filenameLabel);
		
		pathLabel.setForeground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_pathLabel = new GridBagConstraints();
		gbc_pathLabel.anchor = GridBagConstraints.WEST;
		gbc_pathLabel.gridx = 0;
		gbc_pathLabel.gridy = 1;
		add(pathLabel, gbc_pathLabel);
		
	}
	
	/**
	 * Fills in the filename and path labels from the specified File.
	 * @param file The file to set the information from. Specify null to empty the labels.
	 */
	public void setFileInfo(File file){
		
		if(file == null){
			
			filenameLabel.setText("");
			pathLabel.setText("");
			
		} else {
			String filePath = file.getAbsolutePath();
			filenameLabel.setText(FilenameUtils.getName(filePath));
			pathLabel.setText(FilenameUtils.getFullPathNoEndSeparator(filePath));
		}
		
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			
		if(isSelected) {
			
			setBackground(table.getSelectionBackground());
			filenameLabel.setForeground(table.getSelectionForeground());
			pathLabel.setForeground(directoryLabelSelectionForeground);
			
		} else {
			
			Color backgroundColor = (row % 2 == 0 || !alternateRowBackground ? table.getBackground() : oddBackgroundColor);
			
			setBackground(backgroundColor);
			filenameLabel.setForeground(table.getForeground());
			pathLabel.setForeground(directoryLabelSelection);
			
		}
		
		return this;
		
	}
	
}