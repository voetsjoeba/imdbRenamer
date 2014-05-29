package com.voetsjoeba.imdb.renamer.gui.action;

import com.voetsjoeba.imdb.renamer.gui.action.generic.CloseDialogAction;
import com.voetsjoeba.imdb.renamer.gui.generic.ApplicationDialog;
import com.voetsjoeba.imdb.renamer.gui.generic.OkDialog;

/**
 * Action meant to be used on the "OK" button in a dialog; closes the dialog when executed.
 * 
 * @see OkDialog
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class OkCloseDialogAction extends CloseDialogAction {
	
	public OkCloseDialogAction(ApplicationDialog dialog){
		super("OK", OkDialog.EXIT_OK, dialog);
	}
	
}