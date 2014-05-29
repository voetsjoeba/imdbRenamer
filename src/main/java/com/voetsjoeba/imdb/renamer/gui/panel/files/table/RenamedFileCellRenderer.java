package com.voetsjoeba.imdb.renamer.gui.panel.files.table;

import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.JTable;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.domain.FileRenamer;
import com.voetsjoeba.imdb.renamer.domain.exception.RenamingException;
import com.voetsjoeba.imdb.renamer.model.TitleModel;

/**
 * Table cell renderer for the renamed file column.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class RenamedFileCellRenderer extends FileInfoCell {
	
	private static final Color mappingErrorForeground = Color.decode("0xCC0000");
	
	private final FileRenamer renamer;
	private final TitleModel titleModel;
	
	public RenamedFileCellRenderer() {
		renamer = Application.getInstance().getRenamer();
		titleModel = Application.getInstance().getTitleModel();
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		/**
		 * <tt>value</tt> can be either an original File, or null.
		 */
		setFileInfo(null); // clear text
		
		if(value instanceof File){
			
			final File file = (File) value;
			
			if(titleModel.isTitleSelected()){
				
				try {
					
					File renamedFile = renamer.getRenamedFile(file, titleModel.getTitle());
					if(renamedFile.equals(file)){
						
						filenameLabel.setFont(regularLabelFont);
						setFileInfo(file);
						
						
						
					} else {
						
						filenameLabel.setFont(boldLabelFont);
						setFileInfo(renamedFile);
						
					}
					
				}
				catch(RenamingException rex) {
					
					filenameLabel.setFont(regularLabelFont);
					filenameLabel.setText(rex.getMessage());
					filenameLabel.setForeground(mappingErrorForeground);
					
					pathLabel.setText("");
					
				}
				
			}
			
		}
		
		return this;
		
	}
	
}
