package com.voetsjoeba.imdb.renamer.gui.action;

import com.voetsjoeba.imdb.renamer.gui.action.generic.CloseDialogAction;
import com.voetsjoeba.imdb.renamer.gui.generic.ApplicationDialog;
import com.voetsjoeba.imdb.renamer.gui.generic.OkCancelDialog;
import com.voetsjoeba.imdb.renamer.gui.generic.OkDialog;

/**
 * Action meant to be used on the "Cancel" button in a dialog; closes the dialog when executed.
 * 
 * @see OkDialog
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class CancelCloseDialogAction extends CloseDialogAction {
	
	public CancelCloseDialogAction(ApplicationDialog dialog) {
		super("Cancel", OkCancelDialog.EXIT_CANCEL, dialog);
	}
	
}