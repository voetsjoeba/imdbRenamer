package com.voetsjoeba.imdb.renamer.util;

import java.awt.Dialog.ModalityType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voetsjoeba.imdb.renamer.gui.generic.CrashReportDialog;

/**
 * Handler for uncaught exceptions.
 * 
 * @author Jeroen
 */
public class CrashReportDialogExceptionHandler implements Thread.UncaughtExceptionHandler {
	
	private static final CrashReportDialog crashDialog;
	private static final Logger log = LoggerFactory.getLogger(CrashReportDialogExceptionHandler.class);
	
	static {
		crashDialog = new CrashReportDialog();
		crashDialog.setModalityType(ModalityType.APPLICATION_MODAL);
	}
	
	/**
	 * Default constructor; must be defined for this class to be used as Swing's exception handler.
	 */
	public CrashReportDialogExceptionHandler(){
		
	}
	
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		log.error("Uncaught exception in Thread {}: {}", t, e.getMessage());
		doHandleException(e);
	}
	
	// handle Swing exceptions
	public void handle(Throwable e){
		log.error("Uncaught exception in Swing EDT: {}", e.getMessage());
		doHandleException(e);
	}
	
	private void doHandleException(Throwable e){
		
		log.error(e.getMessage(), e); // log detailed information (including stack trace)
		
		// don't show the dialog message again if multiple exceptions are incoming
		if(!crashDialog.isVisible()){
			try {
				crashDialog.reset();
				crashDialog.setException(e);
				crashDialog.setLocationRelativeTo(null); // center
				log.debug("Displaying crash report dialog to user");
				crashDialog.setVisible(true);
			}
			catch(Throwable ex){
				// we need to make damn well sure we don't throw another exception on this thread, otherwise we might end up
				// in an infinite recursion loop and spawning error windows like a crazy person
				e.printStackTrace();
				ex.printStackTrace();
			}
		}
		
	}
	
}
