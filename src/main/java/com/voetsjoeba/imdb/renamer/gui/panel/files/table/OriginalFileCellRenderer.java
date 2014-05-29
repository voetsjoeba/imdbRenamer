package com.voetsjoeba.imdb.renamer.gui.panel.files.table;

import java.awt.Component;
import java.io.File;

import javax.swing.JTable;

/**
 * Table cell renderer for the original file column.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class OriginalFileCellRenderer extends FileInfoCell {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setFileInfo(null);
		
		if(value == null || !(value instanceof File)) return null;
		
		final File file = (File) value;
		setFileInfo(file);
		
		return this;
		
	}
	
}
