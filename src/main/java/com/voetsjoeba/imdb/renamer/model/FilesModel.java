package com.voetsjoeba.imdb.renamer.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.event.FileAddedEvent;
import com.voetsjoeba.imdb.renamer.event.FileListListener;
import com.voetsjoeba.imdb.renamer.event.FileRemovedEvent;
import com.voetsjoeba.imdb.renamer.event.FilesClearedEvent;
import com.voetsjoeba.imdb.util.ListenerUtils;

/**
 * Holds the files that are to be renamed by the application.
 * 
 * @author Jeroen De Ridder
 */
public class FilesModel implements Iterable<File>, ApplicationModel {
	
	private static final Logger log = LoggerFactory.getLogger(FilesModel.class);
	
	private EventListenerList listeners;
	private List<File> files;
	
	public FilesModel(){
		init();
	}
	
	private void init(){
		listeners = new EventListenerList();
		files = new ArrayList<File>();
	}
	
	public int getSize(){
		return files.size();
	}
	
	public void clear(){
		
		files.clear();
		fireFilesCleared(new FilesClearedEvent(this));
		
	}
	
	@SuppressWarnings("unchecked")
	public void add(File file){
		
		if(file == null || contains(file)) return;
		
		List<File> filesToAdd = new ArrayList<File>();
		
		if(file.isFile()){
			
			filesToAdd.add(file);
			
		} else if(file.isDirectory()){
			
			Collection<File> files = FileUtils.listFiles(file, null, false);
			for(File childFile : files) filesToAdd.add(childFile);
			
		} else {
			return;
		}
		
		for(File fileToAdd : filesToAdd){
			
			// TODO: is canWrite the correct permissions needed to rename a file? it's preventing files that are marked as "read-only"
			// from being added.
			if(!fileToAdd.canRead() || !fileToAdd.canWrite()){
				log.info("Cannot read or write file {}, skipping (check for read-only file system flag!)", fileToAdd);
				return;
			}
			
			if(files.add(fileToAdd)){
				//Application.getInstance().log("Added file %s", fileToAdd.getName()); // TODO: crazy slow
				fireFileAdded(new FileAddedEvent(this, fileToAdd));
			}
			
		}
		
	}
	
	public boolean contains(File file){
		return files.contains(file);
	}
	
	public void add(Collection<File> files){
		if(files == null) return;
		for(File file : files) add(file);
	}
	
	public void remove(File file){
		
		if(file == null) return;
		
		if(files.remove(file)){
			Application.getInstance().log("Removed file %s", file.getName());
			fireFileRemoved(new FileRemovedEvent(this, file));
		}
		
	}
	
	public void remove(Collection<File> files){
		if(files == null) return;
		for(File file : files) remove(file);
	}
	
	@Override
	public Iterator<File> iterator() {
		return Collections.unmodifiableList(files).iterator();
	}
	
	public List<File> getFiles(){
		return Collections.unmodifiableList(files);
	}
	
	public void addFileListChangeListener(FileListListener listener){
		listeners.add(FileListListener.class, listener);
	}
	
	protected void fireFileAdded(FileAddedEvent e){
		for(FileListListener l : listeners.getListeners(FileListListener.class)){
			try {
				l.fileAdded(e);
			}
			catch(RuntimeException rex){
				ListenerUtils.handleListenerException(rex, l);
			}
		}
	}
	
	protected void fireFileRemoved(FileRemovedEvent e){
		for(FileListListener l : listeners.getListeners(FileListListener.class)){
			try {
				l.fileRemoved(e);
			}
			catch(RuntimeException rex){
				ListenerUtils.handleListenerException(rex, l);
			}
		}
	}
	
	protected void fireFilesCleared(FilesClearedEvent e){
		for(FileListListener l : listeners.getListeners(FileListListener.class)){
			try {
				l.filesCleared(e);
			}
			catch(RuntimeException rex){
				ListenerUtils.handleListenerException(rex, l);
			}
		}
	}

	public File getFile(int row) {
		return files.get(row);
	}
	
}
