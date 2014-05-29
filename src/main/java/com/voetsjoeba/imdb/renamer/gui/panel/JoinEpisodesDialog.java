package com.voetsjoeba.imdb.renamer.gui.panel;

import java.util.List;

import com.voetsjoeba.imdb.domain.api.Episode;
import com.voetsjoeba.imdb.renamer.gui.generic.OkCancelDialog;

/**
 * Allows users to join multiple selected episodes into a single renaming operation.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class JoinEpisodesDialog extends OkCancelDialog {
	
	@SuppressWarnings("unused")
	private List<Episode> episodes;
	
	public JoinEpisodesDialog(List<Episode> episodes) {
		
		initComponents();
		initLayout();
		initActions();
		
		this.episodes = episodes;
		
	}
	
	private void initComponents(){
		
	}
	
	private void initLayout(){
		
		setTitle("Join episodes:");
		
	}
	
	private void initActions(){
		
	}
	
	public String getJoinedEpisodeNumber(){
		return null;
	}
	
	public String getJoinedTitle(){
		return null;
	}
	
}
