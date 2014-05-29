package com.voetsjoeba.imdb.renamer.gui.panel.files.table;

import java.io.File;

import javax.swing.table.AbstractTableModel;

import com.voetsjoeba.imdb.domain.SeasonEpisodeNumber;
import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.domain.FileRenamer;
import com.voetsjoeba.imdb.renamer.event.DefaultTitleRenameFormatChangedEvent;
import com.voetsjoeba.imdb.renamer.event.FileAddedEvent;
import com.voetsjoeba.imdb.renamer.event.FileListListener;
import com.voetsjoeba.imdb.renamer.event.FileRemovedEvent;
import com.voetsjoeba.imdb.renamer.event.FilenameAnalysisListener;
import com.voetsjoeba.imdb.renamer.event.FilenameAnalysisUpdatedEvent;
import com.voetsjoeba.imdb.renamer.event.FilesClearedEvent;
import com.voetsjoeba.imdb.renamer.event.RenameFormatChangedEvent;
import com.voetsjoeba.imdb.renamer.event.RenameFormatListener;
import com.voetsjoeba.imdb.renamer.event.TitleSelectedEvent;
import com.voetsjoeba.imdb.renamer.event.TitleSelectionListener;
import com.voetsjoeba.imdb.renamer.model.FilesModel;
import com.voetsjoeba.imdb.renamer.model.RenameFormatModel;
import com.voetsjoeba.imdb.renamer.model.TitleModel;

@SuppressWarnings("serial")
public class FileListTableModel extends AbstractTableModel implements FileListListener, TitleSelectionListener, RenameFormatListener, FilenameAnalysisListener, FileListTableConstants {
	
	private final FilesModel filesModel;
	private final TitleModel titleModel;
	private final RenameFormatModel renameFormatModel;
	private final FileRenamer renamer;
	
	public FileListTableModel() {
		
		filesModel = Application.getInstance().getFilesModel();
		renamer = Application.getInstance().getRenamer();
		titleModel = Application.getInstance().getTitleModel();
		renameFormatModel = Application.getInstance().getRenameFormatModel();
		
		init();
		
	}
	
	private void init(){
		
		filesModel.addFileListChangeListener(this);
		renamer.getFilenameAnalyzer().addFilenameAnalysisListener(this);
		titleModel.addTitleSelectionListener(this);
		renameFormatModel.addRenameFormatListener(this);
		
	}
	
	@Override
	public int getRowCount() {
		return filesModel.getSize();
	}
	
	@Override
	public int getColumnCount() {
		//return columnNames.length;
		throw new RuntimeException("We shouldn't get here -- pass a TableColumnModel to the JTable constructor to prevent it from auto-creating one (using this method to find out how many to create)");
	}
	
	@Override
	public String getColumnName(int column) {
		//return columnNames[column];
		throw new RuntimeException("We shouldn't get here -- pass a TableColumnModel to the JTable constructor to prevent it from auto-creating one (using this method to find out how many to create)");
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		
		/**
		 * Be careful what you return here; JTable needs to know an editor cell renderer for the specified class type,
		 * otherwise table rendering will fail with a vague NPE somewhere in the slums of JTable code.
		 */
		return String.class;
	}
	
	@Override
	public Object getValueAt(int row, int column) {
		
		final File file = filesModel.getFile(row);
		final SeasonEpisodeNumber extractedSeNumber = renamer.getFilenameAnalyzer().getFilenameInfo(file).getSeasonEpisodeNumber();
		
		switch(column){
			case CHECKBOX_COLUMN:
				return null;
				
			case ORIGINAL_FILE_COLUMN:
				return file;
				
			case RENAMED_FILE_COLUMN:
				return file;
				
				/*File renamedFile = null;
				
				if(titleModel.isTitleSelected()){
					
					try {
						renamedFile = renamer.getRenamedFile(file, titleModel.getTitle());
						return renamedFile;
					}
					catch(RenamingException e) {
						return e; // return the exception so the renderer can display an error message
					}
					
				}
				return null;*/
			
			case SEASON_NR_COLUMN:
				return (extractedSeNumber == null ? null : extractedSeNumber.getSeasonNumber());
				
			case EPISODE_NR_COLUMN:
				return (extractedSeNumber == null ? null : extractedSeNumber.getEpisodeNumber());
				
			default:
				return "wat";
		}
		
	}
	
	@Override
	public void fileAdded(FileAddedEvent e) {
		fireTableDataChanged();
		fireTableChanged(null);
	}
	
	@Override
	public void fileRemoved(FileRemovedEvent e) {
		fireTableDataChanged();
	}
	
	@Override
	public void filesCleared(FilesClearedEvent e) {
		fireTableDataChanged();
	}
	
	@Override
	public void titleSelected(TitleSelectedEvent e) {
		fireTableDataChanged();
	}
	
	@Override
	public void filenameAnalysisUpdated(FilenameAnalysisUpdatedEvent e) {
		fireTableDataChanged();
	}
	
	@Override
	public void defaultTitleRenameFormatChanged(DefaultTitleRenameFormatChangedEvent e) {
		fireTableDataChanged();
	}
	
	@Override
	public void renameFormatChanged(RenameFormatChangedEvent e) {
		fireTableDataChanged();
	}

}
