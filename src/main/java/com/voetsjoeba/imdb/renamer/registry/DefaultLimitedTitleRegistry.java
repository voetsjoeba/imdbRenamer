package com.voetsjoeba.imdb.renamer.registry;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.event.EventListenerList;

import com.voetsjoeba.imdb.domain.LimitedTitle;
import com.voetsjoeba.imdb.renamer.event.LimitedTitleRegistryListener;
import com.voetsjoeba.imdb.renamer.event.RegistryTitleAddedEvent;
import com.voetsjoeba.imdb.renamer.event.RegistryTitleRemovedEvent;

/**
 * Default {@link LimitedTitleRegistry} implementation. Delegates to a {@link HashMap}. 
 * 
 * @author Jeroen
 */
public class DefaultLimitedTitleRegistry implements LimitedTitleRegistry {
	
	private boolean dirty = false;
	
	protected EventListenerList listeners;
	protected Map<String, LimitedTitle> titles;
	
	public DefaultLimitedTitleRegistry(){
		titles = new HashMap<String, LimitedTitle>();
		listeners = new EventListenerList();
	}
	
	public DefaultLimitedTitleRegistry(File in) throws FileNotFoundException, IOException {
		read(in);
	}
	
	@Override
	public void add(LimitedTitle title){
		
		if(title == null) return;
		String key = title.getId();
		
		if(!titles.containsKey(key)) dirty = true;
		titles.put(key, title);
		
		if(dirty) fireTitleAdded(new RegistryTitleAddedEvent(this, title));
		
	}
	
	@Override
	public void add(Collection<LimitedTitle> titles){
		for(LimitedTitle title : titles) add(title);
	}
	
	@Override
	public void remove(LimitedTitle title){
		
		if(title == null) return;
		String key = title.getId();
		
		if(titles.containsKey(key)) dirty = true;
		titles.remove(key);
		
		if(dirty) fireTitleRemoved(new RegistryTitleRemovedEvent(this, title));
		
	}
	
	@Override
	public void remove(Collection<LimitedTitle> titles){
		for(LimitedTitle title : titles) remove(title);
	}
	
	@Override
	public boolean contains(String titleId){
		return titles.containsKey(titleId);
	}
	
	@Override
	public LimitedTitle get(String titleId){
		return titles.get(titleId);
	}
	
	@Override
	public Collection<LimitedTitle> getTitles(){
		return Collections.unmodifiableCollection(titles.values());
	}
	
	@Override
	public int size() {
		return titles.size();
	}
	
	@Override
	public Iterator<LimitedTitle> iterator() {
		return titles.values().iterator();
	}
	
	// -------------------------------------------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	protected void read(InputStream in) throws IOException {
		
		ObjectInputStream objectIn = new ObjectInputStream(in);
		
		try {
			titles = (HashMap<String, LimitedTitle>) objectIn.readObject();
		}
		catch(ClassCastException ccex){
			throw new IOException("Unexpected title registry file format", ccex);
		}
		catch(IllegalStateException isex){
			throw new IOException("Unexpected title registry file format", isex);
		}
		catch(ClassNotFoundException cnfex){
			throw new IOException("Unexpected title registry file format", cnfex);
		}
		
	}
	
	@Override
	public void read(File in) throws FileNotFoundException, IOException {
		InputStream inputStream = new BufferedInputStream(new FileInputStream(in));
		read(inputStream);
		inputStream.close();
	}
	
	protected void write(OutputStream out) throws IOException {
		ObjectOutputStream objectOut = new ObjectOutputStream(out);
		objectOut.writeObject(titles);
	}
	
	@Override
	public boolean isDirty(){
		return dirty;
	}
	
	@Override
	public void write(File out) throws FileNotFoundException, IOException {
		OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(out));
		write(outputStream);
		outputStream.close();
	}
	
	// -------------------------------------------------------------------------------------------------
	
	@Override
	public void addLimitedTitleRegistryListener(LimitedTitleRegistryListener l){
		listeners.add(LimitedTitleRegistryListener.class, l);
	}
	
	@Override
	public void removeLimitedTitleRegistryListener(LimitedTitleRegistryListener l){
		listeners.remove(LimitedTitleRegistryListener.class, l);
	}
	
	protected void fireTitleAdded(RegistryTitleAddedEvent e){
		for(LimitedTitleRegistryListener listener : listeners.getListeners(LimitedTitleRegistryListener.class)){
			listener.registryTitleAdded(e);
		}
	}
	
	protected void fireTitleRemoved(RegistryTitleRemovedEvent e){
		for(LimitedTitleRegistryListener listener : listeners.getListeners(LimitedTitleRegistryListener.class)){
			listener.registryTitleRemoved(e);
		}
	}
	
	// -------------------------------------------------------------------------------------------------
	
	@Override
	public Set<LimitedTitle> getApproximateMatches(String filename){
		
		Set<LimitedTitle> result = new HashSet<LimitedTitle>();
		
		
		
		// TODO: implement me
		//   - first split the search terms into words (ie. split on punctuation etc)
		//     drop common words like XVID, 720p, divx, release group names, season/episode specifier, ...
		//   - then I guess ...
		//     o either find the title somehow and match that against the titles in the registry
		//     o either do a word-by-word match against the words of titles in the registry
		//   - then do it again, this time leaving out all the vowels (titles are sometimes written without the vowels
		//     to trip up any automatic copyright violation checkers), see if anything gets a higher match than before
		//   - present the X best matches to the user
		return result;
		
	}
	
}
