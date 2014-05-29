package com.voetsjoeba.imdb.renamer.gui.panel.files.table;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;


@SuppressWarnings("serial")
public class FileListTableColumnModel extends DefaultTableColumnModel implements FileListTableConstants {
	
	public FileListTableColumnModel() {
		super();
		init();
	}
	
	private void init() {
		
		TableColumn checkboxColumn = new TableColumn(CHECKBOX_COLUMN, 30);
		TableColumn filenameColumn = new TableColumn(ORIGINAL_FILE_COLUMN);
		TableColumn renamedColumn = new TableColumn(RENAMED_FILE_COLUMN);
		TableColumn seasonColumn = new TableColumn(SEASON_NR_COLUMN, 30);
		TableColumn episodeColumn = new TableColumn(EPISODE_NR_COLUMN, 30);
		
		checkboxColumn.setHeaderValue("");
		seasonColumn.setHeaderValue("S");
		episodeColumn.setHeaderValue("E");
		filenameColumn.setHeaderValue("Filename");
		renamedColumn.setHeaderValue("Renamed");
		
		checkboxColumn.setMinWidth(22);
		seasonColumn.setMinWidth(22);
		episodeColumn.setMinWidth(22);
		
		checkboxColumn.setMaxWidth(22);
		seasonColumn.setMaxWidth(22);
		episodeColumn.setMaxWidth(22);
		
		addColumn(checkboxColumn);
		addColumn(filenameColumn);
		addColumn(renamedColumn);
		addColumn(seasonColumn);
		addColumn(episodeColumn);
		
		filenameColumn.setCellRenderer(new OriginalFileCellRenderer());
		renamedColumn.setCellRenderer(new RenamedFileCellRenderer());
		
	}
	
}
