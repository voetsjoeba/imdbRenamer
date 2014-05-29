package com.voetsjoeba.imdb.renamer.gui.action.generic;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.gui.action.AboutAction;
import com.voetsjoeba.imdb.renamer.gui.action.ClearFilesAction;
import com.voetsjoeba.imdb.renamer.gui.action.OpenFilesAction;
import com.voetsjoeba.imdb.renamer.gui.action.OpenFolderAction;
import com.voetsjoeba.imdb.renamer.gui.action.OptionsAction;
import com.voetsjoeba.imdb.renamer.gui.action.PerformRenameAction;
import com.voetsjoeba.imdb.renamer.gui.action.RemoveUnaffectedFilesAction;
import com.voetsjoeba.imdb.renamer.gui.action.RenameFormatDialogAction;
import com.voetsjoeba.imdb.renamer.gui.action.ViewTitleRegistryAction;


/**
 * Holds singleton actions.
 * 
 * @author Jeroen De Ridder
 */
public class ActionSingleton {
	
	private static PerformRenameAction performRenameAction;
	private static ClearFilesAction clearFilesAction;
	private static OpenFilesAction openFilesAction;
	private static OpenFolderAction openFolderAction;
	private static ExitAction exitAction;
	private static RemoveUnaffectedFilesAction removeUnaffectedFilesAction;
	private static ViewTitleRegistryAction viewTitleRegistryAction;
	private static OptionsAction settingsAction;
	private static AboutAction aboutAction;
	private static RenameFormatDialogAction renameFormatsDialogAction;
	
	public static PerformRenameAction getPerformRenameAction(){
		
		if(performRenameAction == null){
			
			performRenameAction = new PerformRenameAction();
			
			performRenameAction.updateEnabledStatus();
			Application.getInstance().getFilesModel().addFileListChangeListener(performRenameAction);
			Application.getInstance().getTitleModel().addTitleSelectionListener(performRenameAction);
			Application.getInstance().getRenamer().getFilenameAnalyzer().addFilenameAnalysisListener(performRenameAction);
			
		}
		
		return performRenameAction;
		
	}
	
	public static ClearFilesAction getClearFilesAction(){
		
		if(clearFilesAction == null){
			
			clearFilesAction = new ClearFilesAction();
			
			clearFilesAction.updateEnabledStatus();
			Application.getInstance().getFilesModel().addFileListChangeListener(clearFilesAction);
			
		}
		
		return clearFilesAction;
		
	}
	
	public static OpenFilesAction getOpenFilesAction(){
		
		if(openFilesAction == null){
			openFilesAction = new OpenFilesAction();
		}
		
		return openFilesAction;
		
	}
	
	public static OpenFolderAction getOpenFolderAction(){
		
		if(openFolderAction == null){
			openFolderAction = new OpenFolderAction();
		}
		
		return openFolderAction;
		
	}
	
	public static ExitAction getExitAction(){
		
		if(exitAction == null){
			exitAction = new ExitAction();
		}
		
		return exitAction;
		
	}
	
	public static RemoveUnaffectedFilesAction getRemoveUnaffectedFilesAction(){
		
		if(removeUnaffectedFilesAction == null){
			
			removeUnaffectedFilesAction = new RemoveUnaffectedFilesAction();
			
			removeUnaffectedFilesAction.updateEnabledStatus();
			Application.getInstance().getTitleModel().addTitleSelectionListener(removeUnaffectedFilesAction);
			Application.getInstance().getFilesModel().addFileListChangeListener(removeUnaffectedFilesAction);
			
		}
		
		return removeUnaffectedFilesAction;
		
	}
	
	public static ViewTitleRegistryAction getViewTitleRegistryAction(){
		
		if(viewTitleRegistryAction == null){
			viewTitleRegistryAction = new ViewTitleRegistryAction();
		}
		
		return viewTitleRegistryAction;
		
	}
	
	public static OptionsAction getSettingsAction(){
		
		if(settingsAction == null){
			settingsAction = new OptionsAction();
		}
		
		return settingsAction;
		
	}
	
	public static AboutAction getAboutAction(){
		
		if(aboutAction == null){
			aboutAction = new AboutAction();
		}
		
		return aboutAction;
		
	}
	
	public static RenameFormatDialogAction getRenameFormatsDialogAction(){
		
		if(renameFormatsDialogAction == null){
			renameFormatsDialogAction = new RenameFormatDialogAction();
		}
		
		return renameFormatsDialogAction;
		
	}
	
}
