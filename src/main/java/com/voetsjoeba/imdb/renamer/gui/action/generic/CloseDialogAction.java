package com.voetsjoeba.imdb.renamer.gui.action.generic;

import java.awt.event.ActionEvent;

import com.voetsjoeba.imdb.renamer.gui.generic.ApplicationDialog;
import com.voetsjoeba.imdb.renamer.gui.generic.ApplicationDialogConstants;
import com.voetsjoeba.imdb.renamer.gui.generic.OkCancelDialog;
import com.voetsjoeba.imdb.renamer.gui.generic.OkDialog;

/**
 * Closes a dialog when executed.
 * 
 * @see OkDialog
 * @see OkCancelDialog
 * @see ApplicationDialogConstants
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class CloseDialogAction extends ApplicationAction {
	
	private final ApplicationDialog dialog;
	
	/**
	 * Exit code to set on the dialog before closing. See {@link ApplicationDialogConstants} for possible values.
	 */
	private final int exitCode;
	
	/**
	 * 
	 * @param name Name of this action, as displayed on buttons etc.
	 * @param exitCode exit code to set on the dialog before closing. See {@link ApplicationDialogConstants} for possible values.
	 * @param dialog the dialog to close when executed
	 */
	public CloseDialogAction(String name, int exitCode, ApplicationDialog dialog){
		super(name);
		this.dialog = dialog;
		this.exitCode = exitCode;
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		dialog.setExitCode(exitCode);
		dialog.close();
	}

	public int getExitCode() {
		return exitCode;
	}
	
}