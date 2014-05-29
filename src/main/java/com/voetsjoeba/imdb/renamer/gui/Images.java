package com.voetsjoeba.imdb.renamer.gui;

import java.net.URL;

import javax.swing.ImageIcon;

/**
 * Holds GUI images.
 * 
 * @author Jeroen De Ridder
 */
public class Images {
	
	public static ImageIcon getBlankIcon(){ return NONE; }
	public static ImageIcon getTreeNoneIcon(){ return NONE_TREE; }
	public static ImageIcon getApplicationIcon(){ return APPLICATION; }
	public static ImageIcon getImdbIcon(){ return IMDB; }
	public static ImageIcon getAboutIcon() { return BOOK_OPEN; }
	public static ImageIcon getOpenFilesIcon(){ return FOLDER; }
	public static ImageIcon getOpenFolderIcon(){ return FOLDER; }
	public static ImageIcon getRemoveFileIcon(){ return CROSS; }
	public static ImageIcon getSearchIcon(){ return MAGNIFIER; }
	public static ImageIcon getRemoveCachedTitleIcon(){ return CROSS; }
	public static ImageIcon getRemoveUnaffectedTitlesIcon() { return BIN; }
	public static ImageIcon getClearLogIcon(){ return CLEAR; }
	public static ImageIcon getOptionsIcon(){ return COG; }
	public static ImageIcon getRenameIcon(){ return TEXTFIELD_RENAME; }
	public static ImageIcon getErrorIcon(){ return ERROR; }
	
	public static ImageIcon getAboutDialogIcon() { return ABOUT; }
	public static ImageIcon getDropzoneIcon() { return DRAG_SHIT_HERE; }
	public static ImageIcon getNameIcon(){ return NAME; }
	public static ImageIcon getLoadingAnimationIcon(){ return LOADING_ANIMATION; }
	public static ImageIcon getEmptySearchResultsIcon(){ return EMPTY_SEARCH_RESULTS; }
	
	private static final ImageIcon NONE;
	private static final ImageIcon NONE_TREE;
	
	// images
	private static final ImageIcon DRAG_SHIT_HERE;
	private static final ImageIcon ABOUT;
	private static final ImageIcon NAME;
	private static final ImageIcon LOADING_ANIMATION;
	private static final ImageIcon EMPTY_SEARCH_RESULTS;
	
	// icons
	private static final ImageIcon APPLICATION;
	private static final ImageIcon IMDB;
	private static final ImageIcon BIN;
	private static final ImageIcon CROSS;
	private static final ImageIcon MAGNIFIER;
	private static final ImageIcon FOLDER;
	private static final ImageIcon BOOK_OPEN;
	private static final ImageIcon CLEAR;
	private static final ImageIcon COG;
	private static final ImageIcon TEXTFIELD_RENAME;
	private static final ImageIcon ERROR;
	
	static {
		
		NONE = new BlankIcon();
		NONE_TREE = new BlankIcon(3, 16);
		
		DRAG_SHIT_HERE = getImage("drag_shit_here.png");
		ABOUT = getImage("about.jpg");
		NAME = getImage("name.png");
		LOADING_ANIMATION = getImage("loading.gif");
		EMPTY_SEARCH_RESULTS = getImage("empty_set.png"); // TODO: i accidentally saved this img with a white bg instead of a transparant one >_>
		
		APPLICATION = getImage("app.png");
		IMDB = getImage("imdb.png");
		CROSS = getImage("cross.png");
		MAGNIFIER = getImage("magnifier.png");
		FOLDER = getImage("folder.png");
		BOOK_OPEN = getImage("book_open.png");
		BIN = getImage("bin.png");
		CLEAR = getImage("clear.png");
		COG = getImage("cog.png");
		ERROR = getImage("cancel.png");
		TEXTFIELD_RENAME = getImage("textfield_rename.png");
		
	}
	
	/** Private constructor, class must not be instantiated */
	private Images(){
		
	}
	
	private static ImageIcon getImage(String filename){
		
		URL resource = Images.class.getResource("/images/" + filename);
		
		if(resource == null){
			throw new IllegalArgumentException("Failed to load image resource " + filename); // so we know an image failed; otherwise, you get a vague NPE out of nowhere in some AWT function that tries to create a greyed out icon
		}
		
		return new ImageIcon(resource);
		
	}
	
}
