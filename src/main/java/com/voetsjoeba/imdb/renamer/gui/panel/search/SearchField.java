package com.voetsjoeba.imdb.renamer.gui.panel.search;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.voetsjoeba.imdb.domain.LimitedTitle;
import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.event.LimitedTitleRegistryListener;
import com.voetsjoeba.imdb.renamer.event.RegistryTitleAddedEvent;
import com.voetsjoeba.imdb.renamer.event.RegistryTitleRemovedEvent;

@SuppressWarnings("serial")
public class SearchField extends JComboBox implements LimitedTitleRegistryListener {
	
	// TODO: turn into autocompleter
	protected DefaultComboBoxModel comboBoxModel;
	
	public SearchField(){
		init();
	}
	
	private void init(){
		
		comboBoxModel = new DefaultComboBoxModel();
		for(LimitedTitle cachedTitle : Application.getInstance().getTitleRegistry().getTitles()){
			comboBoxModel.addElement(cachedTitle);
		}
		comboBoxModel.setSelectedItem(null);
		
		setModel(comboBoxModel);
		setEditable(true);
		
		Application.getInstance().getTitleRegistry().addLimitedTitleRegistryListener(this);
		
	}

	@Override
	public void registryTitleAdded(RegistryTitleAddedEvent e) {
		comboBoxModel.addElement(e.getObject());
	}
	
	@Override
	public void registryTitleRemoved(RegistryTitleRemovedEvent e) {
		comboBoxModel.removeElement(e.getObject());
	}
	
}
