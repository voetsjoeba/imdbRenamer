package com.voetsjoeba.imdb.renamer.gui.action;

import java.awt.event.ActionEvent;
import java.util.Collection;

import com.voetsjoeba.imdb.domain.LimitedTitle;
import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.gui.Images;
import com.voetsjoeba.imdb.renamer.gui.action.generic.ApplicationAction;

/**
 * Removes one or more titles from the title registry.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class LimitedTitleRegistryRemoveAction extends ApplicationAction {
	
	protected final Collection<LimitedTitle> titlesToRemove;
	
	public LimitedTitleRegistryRemoveAction(Collection<LimitedTitle> titlesToRemove){
		
		super("Remove", Images.getRemoveCachedTitleIcon());
		this.titlesToRemove = titlesToRemove;
		
		setTooltip("Rmove title(s) from the title registry.");
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Application.getInstance().getTitleRegistry().remove(titlesToRemove);
	}
	
}
