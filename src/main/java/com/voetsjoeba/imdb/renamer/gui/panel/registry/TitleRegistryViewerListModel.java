package com.voetsjoeba.imdb.renamer.gui.panel.registry;

import javax.swing.DefaultListModel;

import com.voetsjoeba.imdb.domain.LimitedTitle;
import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.event.LimitedTitleRegistryListener;
import com.voetsjoeba.imdb.renamer.event.RegistryTitleAddedEvent;
import com.voetsjoeba.imdb.renamer.event.RegistryTitleRemovedEvent;
import com.voetsjoeba.imdb.renamer.registry.LimitedTitleRegistry;

/**
 * Model for a {@link TitleRegistryViewerList}.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class TitleRegistryViewerListModel extends DefaultListModel implements LimitedTitleRegistryListener {
	
	public TitleRegistryViewerListModel(){
		
		LimitedTitleRegistry registry = Application.getInstance().getTitleRegistry();
		for(LimitedTitle title : registry){
			addElement(title);
		}
		
		registry.addLimitedTitleRegistryListener(this);
		
	}
	
	@Override
	public void registryTitleAdded(RegistryTitleAddedEvent e) {
		addElement(e.getObject());
	}
	
	@Override
	public void registryTitleRemoved(RegistryTitleRemovedEvent e) {
		removeElement(e.getObject());
	}
	
}
