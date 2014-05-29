package com.voetsjoeba.imdb.renamer.gui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collection;

import javax.swing.AbstractAction;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.domain.analysis.FilenameAnalyzer;
import com.voetsjoeba.imdb.renamer.domain.analysis.FilenameInfo;

/**
 * Removes any mappings of a collection of files.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class RemoveSeasonEpisodeNumberAction extends AbstractAction {
	
	// TODO: removed mapping gets remapped automatically :(
	
	protected Collection<File> files;
	
	public RemoveSeasonEpisodeNumberAction(Collection<File> files){
		super("Remove Season/Episode number");
		this.files = files;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		FilenameAnalyzer mapper = Application.getInstance().getRenamer().getFilenameAnalyzer();
		
		// remove them mappings
		for(File file : files){
			
			FilenameInfo filenameInfo = mapper.getFilenameInfo(file);
			filenameInfo.setSeasonEpisodeNumber(null);
			
		}
		
	}
	
}
