package com.voetsjoeba.imdb.renamer.domain.analysis;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.voetsjoeba.imdb.domain.SeasonEpisodeNumber;
import com.voetsjoeba.imdb.util.ListenerUtils;

/**
 * Represents information extracted from an abstract file path of a movie/series release.
 * 
 * @author Jeroen
 */
public class FilenameInfo {
	
	
	
	protected Integer year;
	protected List<String> titleWords;
	protected SeasonEpisodeNumber seasonEpisodeNumber;
	protected String releaseGroup;
	protected String videoCodec;
	protected String audioCodec;
	protected String source;
	protected String videoFormat;
	
	protected final File file;
	protected final PropertyChangeSupport propertyChangeSupport;
	
	public FilenameInfo(File file) {
		this.file = file;
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	/**
	 * Returns the series/movie title as extracted from the file name (if applicable).
	 */
	public String getTitle(){
		return getTitle(" ");
	}
	
	/**
	 * Same as {@link #getTitle()}, except uses a custom separator between the title words.
	 * 
	 * @see #getTitleWords()
	 */
	public String getTitle(String separator){
		if(titleWords == null) return null;
		return StringUtils.join(titleWords, separator);
	}
	
	/**
	 * Returns the abstract path of the file this information applies to.
	 */
	public File getFile(){
		return file;
	}
	
	// ----------------------------------------------------------------------------------
	
	public Integer getYear() {
		return year;
	}
	
	public List<String> getTitleWords() {
		return titleWords;
	}
	
	public SeasonEpisodeNumber getSeasonEpisodeNumber() {
		return seasonEpisodeNumber;
	}
	
	public String getReleaseGroup() {
		return releaseGroup;
	}
	
	public String getVideoCodec() {
		return videoCodec;
	}
	
	public String getAudioCodec() {
		return audioCodec;
	}
	
	public String getSource() {
		return source;
	}
	
	public String getVideoFormat() {
		return videoFormat;
	}
	
	// ----------------------------------------------------------------------------------
	
	public void setYear(Integer year) {
		this.year = year;
		firePropertyChange("year", this.year, year);
	}
	
	public void setTitleWords(List<String> titleWords) {
		firePropertyChange("titleWords", this.titleWords, this.titleWords = titleWords);
	}
	
	public void setSeasonEpisodeNumber(SeasonEpisodeNumber seasonEpisodeNumber) {
		//this.seasonEpisodeNumber = seasonEpisodeNumber;
		firePropertyChange("seasonEpisodeNumber", this.seasonEpisodeNumber, this.seasonEpisodeNumber = seasonEpisodeNumber);
	}
	
	public void setReleaseGroup(String releaseGroup) {
		this.releaseGroup = releaseGroup;
		firePropertyChange("releaseGroup", this.releaseGroup, releaseGroup);
	}
	
	public void setVideoCodec(String videoCodec) {
		this.videoCodec = videoCodec;
		firePropertyChange("videoCodec", this.videoCodec, videoCodec);
	}
	
	public void setAudioCodec(String audioCodec) {
		this.audioCodec = audioCodec;
		firePropertyChange("audioCodec", this.audioCodec, audioCodec);
	}
	
	public void setSource(String source) {
		this.source = source;
		firePropertyChange("source", this.source, source);
	}
	
	public void setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
		firePropertyChange("videoFormat", this.videoFormat, videoFormat);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener){
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	public void removeGlobalPropertyChangeListener(PropertyChangeListener listener){
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		try {
			propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
		}
		catch(RuntimeException rex){
			ListenerUtils.handleListenerException(rex, null);
		}
	}
	
	public void firePropertyChange(String propertyName, int oldValue, int newValue) {
		try {
			propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
		}
		catch(RuntimeException rex){
			ListenerUtils.handleListenerException(rex, null);
		}
	}
	
	public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		try {
			propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
		}
		catch(RuntimeException rex){
			ListenerUtils.handleListenerException(rex, null);
		}
	}
	
}
