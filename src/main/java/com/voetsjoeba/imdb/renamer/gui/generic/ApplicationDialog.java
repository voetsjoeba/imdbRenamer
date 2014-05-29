package com.voetsjoeba.imdb.renamer.gui.generic;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;

import com.voetsjoeba.imdb.renamer.Application;

/**
 * Base class for all dialogs in the application. Implements various listener interfaces for ease of subclassing.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class ApplicationDialog extends JDialog implements ApplicationDialogConstants, WindowListener {
	
	/**
	 * One of the exit codes specified in {@link ApplicationDialogConstants}.
	 */
	private int exitCode = 0;
	
	public ApplicationDialog(){
		this(Application.getInstance().getMainWindow());
	}
	
	public ApplicationDialog(JFrame owner){
		super(owner);
	}
	
	/**
	 * Closes the dialog.
	 */
	public void close(){
		dispose();
	}
	
	public void setIcon(ImageIcon imageIcon){
		
		Window owner = getOwner();
		
		if(owner instanceof Frame){
			Frame ownerFrame = (Frame) owner;
			ownerFrame.setIconImage(imageIcon.getImage());
		}
		
	}
	
	/**
	 * Sets the exit code for this dialog. See {@link ApplicationDialogConstants} for possible values.
	 */
	public void setExitCode(int exitCode){
		this.exitCode = exitCode;
	}
	
	/**
	 * Returns the exit code this dialog was closed with. See {@link ApplicationDialogConstants} for possible values.
	 */
	public int getExitCode(){
		return exitCode;
	}

	@Override public void windowOpened(WindowEvent e) { }
	@Override public void windowClosing(WindowEvent e) { }
	@Override public void windowClosed(WindowEvent e) { }
	@Override public void windowIconified(WindowEvent e) { }
	@Override public void windowDeiconified(WindowEvent e) { }
	@Override public void windowActivated(WindowEvent e) { }
	@Override public void windowDeactivated(WindowEvent e) { }
	
}
