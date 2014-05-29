package com.voetsjoeba.imdb.renamer.gui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import com.voetsjoeba.imdb.domain.api.Episode;
import com.voetsjoeba.imdb.domain.api.Series;
import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.domain.analysis.FilenameInfo;
import com.voetsjoeba.imdb.renamer.gui.action.generic.ApplicationAction;
import com.voetsjoeba.imdb.renamer.gui.panel.SeriesMappingDialog;

/**
 * Assigns a season/episode number to a file manually.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class AssignSeasonEpisodeNumberAction extends ApplicationAction {
	
	protected File file;
	protected Series series;
	
	/**
	 * Creates an assignment action based on a file and a {@link Series} object. When executed, the action will display a dialog
	 * menu holding a combobox prepopulated with episode numbers from the provided <i>series</i>.
	 * @param file the file to assign the season/episode number to
	 * @param series the {@link Series} object to grab the possible season/episode numbers from
	 */
	public AssignSeasonEpisodeNumberAction(File file, Series series){
		
		super("Assign Season/Episode number");
		
		this.file = file;
		this.series = series;
		
		setTooltip("Assigns a season and an episode number to this file, overriding any previous value.");
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		SeriesMappingDialog seNumberDialog = new SeriesMappingDialog(series);
		seNumberDialog.setModal(true);
		seNumberDialog.setVisible(true);
		
		List<Episode> selectedItems = seNumberDialog.getSelectedItems();
		
		if(selectedItems.size() <= 0) return;
		
		if(selectedItems.size() >= 1){
			
			Episode selectedEpisode = selectedItems.get(0);
			FilenameInfo filenameInfo = Application.getInstance().getRenamer().getFilenameAnalyzer().getFilenameInfo(file);
			filenameInfo.setSeasonEpisodeNumber(selectedEpisode.getSeasonEpisodeNumber());
			
		}
		
		/*if(selectedItems.size() <= 0){
			
			return;
			
		} else if(selectedItems.size() == 1){
			
			Object selectedItem = selectedItems.get(0);
			
			if(selectedItem instanceof Episode){
				Episode selectedEpisode = (Episode) selectedItem;
				Application.getInstance().getRenamer().getSeasonEpisodeMapper().assignSeasonEpisodeNumber(file, selectedEpisode.getSeasonEpisodeNumber());
			}
			
		} else {
			
			List<Episode> joinEpisodes = new ArrayList<Episode>();
			for(Object selectedItem : selectedItems){
				if(selectedItem instanceof Episode) joinEpisodes.add((Episode) selectedItem);
			}
			
			// multiple episodes selected, join them together
			JoinEpisodesDialog joinDialog = new JoinEpisodesDialog(joinEpisodes);
			joinDialog.setModal(true);
			joinDialog.setVisible(true);
			
			if(joinDialog.getExitCode() == OkDialog.OK){
				
				String joinedEpisodeNumber = joinDialog.getJoinedEpisodeNumber();
				String joinedTitle = joinDialog.getJoinedTitle();
				
				// TODO: figure out a way to conceptually merge the idea of a joined episode number "eg. 01-02-03" with a ":02" modifier on ${episodeNr}
				
			}
			
		}*/
		
		/*Object selectedItem = seNumberDialog.getSelectedItem();
		if(selectedItem instanceof Episode){
			Episode selectedEpisode = (Episode) selectedItem;
			Application.getInstance().getRenamer().getSeasonEpisodeMapper().assignSeasonEpisodeNumber(file, selectedEpisode.getSeasonEpisodeNumber());
		}*/
		
	}
	
}
