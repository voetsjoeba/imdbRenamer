package com.voetsjoeba.imdb.renamer.event;

import java.util.EventListener;

import com.voetsjoeba.imdb.renamer.model.FilesModel;

/**
 * Listener for changes to the {@link FilesModel} state.
 * 
 * @see FilesModel
 * @author Jeroen De Ridder
 */
public interface FileListListener extends EventListener {
	
	/**
	 * Indicates that a new file was added to the {@link FilesModel}.
	 */
	public void fileAdded(FileAddedEvent e);
	
	/**
	 * Indicates that a file was removed from the {@link FilesModel}. Note that these events are <i>not</i> fired when 
	 * the model is cleared! Use the {@link #filesCleared(FilesClearedEvent)} handler for these events.
	 */
	public void fileRemoved(FileRemovedEvent e);
	
	/**
	 * Indicates that all files were removed from the {@link FilesModel}. This event is not fired if the last item in the
	 * {@link FilesModel} just happens to be removed; clearing the model is seen as a separate event.
	 */
	// TODO: remove this event handler and delegate clearing to remove() calls
	public void filesCleared(FilesClearedEvent e);
	
}
