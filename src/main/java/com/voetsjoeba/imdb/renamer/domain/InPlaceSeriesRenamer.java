package com.voetsjoeba.imdb.renamer.domain;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voetsjoeba.imdb.domain.SeasonEpisodeNumber;
import com.voetsjoeba.imdb.domain.api.Episode;
import com.voetsjoeba.imdb.domain.api.Season;
import com.voetsjoeba.imdb.domain.api.Series;
import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.domain.analysis.FilenameAnalyzer;
import com.voetsjoeba.imdb.renamer.domain.analysis.FilenameInfo;
import com.voetsjoeba.imdb.renamer.domain.analysis.StandardFilenameAnalyzer;
import com.voetsjoeba.imdb.renamer.domain.exception.IllegalFilenameException;
import com.voetsjoeba.imdb.renamer.domain.exception.InterpolationException;
import com.voetsjoeba.imdb.renamer.domain.exception.NoEpisodeMappingException;
import com.voetsjoeba.imdb.renamer.domain.exception.NoSuchEpisodeException;
import com.voetsjoeba.imdb.renamer.domain.exception.RenamingException;
import com.voetsjoeba.imdb.renamer.domain.interpolation.HashMapInterpolationArguments;
import com.voetsjoeba.imdb.renamer.domain.interpolation.InterpolationArguments;
import com.voetsjoeba.imdb.renamer.domain.interpolation.InterpolationResults;
import com.voetsjoeba.imdb.renamer.domain.interpolation.RenameFormatInterpolator;
import com.voetsjoeba.imdb.renamer.domain.interpolation.StringInterpolator;
import com.voetsjoeba.imdb.renamer.model.RenameFormatModel;
import com.voetsjoeba.imdb.renamer.model.TitleModel;

/**
 * Renames files in place (i.e. keeps them within their original directory) based on a {@link Series} object.
 * 
 * @author Jeroen De Ridder
 */
public class InPlaceSeriesRenamer extends AbstractFileRenamer {
	
	private static final Logger log = LoggerFactory.getLogger(InPlaceSeriesRenamer.class);
	
	//protected String renameFormat;
	protected String filenameConflictFormat;
	protected int maxConflictTries;
	
	protected boolean renameOnConflict = true;
	//protected String defaultTitleRenameFormat = null;
	
	protected StringInterpolator stringInterpolator;
	protected FilenameAnalyzer filenameAnalyzer;
	
	/**
	 * Creates a new InPlaceSeriesRenamer object.
	 * @param renameOnConflict whether or not to rename the new filename if conflicts occur. "Deconflicted" filenames will be of the form "original.ext", "original (1).ext", "original (2).ext" etc., successively.
	 */
	public InPlaceSeriesRenamer(boolean renameOnConflict){ // TODO: create Strategy object or sth for handling rename conflicts
		
		//this.renameFormat = renameFormat;
		this.renameOnConflict = renameOnConflict;
		this.filenameConflictFormat = "%s (%d)";
		this.maxConflictTries = 250;
		
		setStringInterpolator(new RenameFormatInterpolator());
		setFilenameAnalyzer(new StandardFilenameAnalyzer());
		
	}
	
	/**
	 * Defaults the <i>renameFormat</i> parameter to true.
	 */
	public InPlaceSeriesRenamer(){
		this(true);
	}
	
	/**
	 * Gets the format string used for generating alternative filenames when a filename conflict occurs.
	 */
	public String getConflictFormat() {
		return filenameConflictFormat;
	}
	
	/**
	 * Sets the format string to be used for generating alternative filenames when a filename conflict occurs.
	 * (follows the String.format syntax; must contain placeholders %s and %d for original (non-renamed) name and try, respectively.
	 */
	public void setConflictFormat(String conflictFormat) {
		this.filenameConflictFormat = conflictFormat;
	}
	
	public boolean getRenameOnConflict() {
		return renameOnConflict;
	}
	
	public void setRenameOnConflict(boolean renameOnConflict) {
		this.renameOnConflict = renameOnConflict;
	}
	
	public int getMaxConflictTries() {
		return maxConflictTries;
	}
	
	public void setMaxConflictTries(int maxConflictTries) {
		this.maxConflictTries = maxConflictTries;
	}
	
	@Override
	public FilenameAnalyzer getFilenameAnalyzer() {
		return filenameAnalyzer;
	}
	
	@Override
	public void setFilenameAnalyzer(FilenameAnalyzer seasonEpisodeMapper) {
		this.filenameAnalyzer = seasonEpisodeMapper;
	}
	
	public StringInterpolator getStringInterpolator() {
		return stringInterpolator;
	}
	
	public void setStringInterpolator(StringInterpolator stringInterpolator) {
		this.stringInterpolator = stringInterpolator;
	}
	
	/**
	 * {@inheritDoc}
	 * @param title expected to be a {@link Series} object.
	 * @throws UnsupportedOperationException if <i>title</i> is not a {@link Series} object
	 * @throws NoEpisodeMappingException if no season or episode number could be determined for <i>file</i>
	 * @throws NoSuchEpisodeException if the currently selected IMDb title (see {@link TitleModel}) does not contain the episode number extracted from <i>file</i>
	 * 
	 * @see FileRenamer#getRenamedFile(File, Title)
	 */
	@Override
	public File getRenamedFile(File file, Title title) throws NoEpisodeMappingException, NoSuchEpisodeException, RenamingException {
		
		String newFilenameNoExt = getRenamedBasename(file, title);
		
		// ------------------------------------------------------------------------------------------------
		
		File destination = null;
		
		try {
			
			String filename = file.getName();
			String extension = FilenameUtils.getExtension(filename);
			
			String newFilename = newFilenameNoExt + "." + extension;
			newFilename = com.voetsjoeba.imdb.renamer.util.FilenameUtils.safeFilename(newFilename);
			
			destination = new File(file.getParentFile(), newFilename);
			boolean renameNecessary = !(filename.equals(newFilename));
			
			if(renameNecessary && renameOnConflict){ // do a check for an existing file name if renameOnConflict is true, but only if there is an actual conflict (as opposed to the renaming generating the same filename)
				
				int attempt = 1;
				while(destination.isFile() && attempt < maxConflictTries){
					
					String newFilenameDeconflictedNoExt = String.format(filenameConflictFormat, newFilenameNoExt, attempt);
					String newFilenameDeconflicted = newFilenameDeconflictedNoExt + "." + extension;
					
					destination = new File(file.getParentFile(), com.voetsjoeba.imdb.renamer.util.FilenameUtils.safeFilename(newFilenameDeconflicted));
					attempt++;
					
				}
				
			}
			
		}
		catch(IllegalFilenameException ifnex){
			// destination remains null
			log.info("Illegal renamed filename exception: {}", ifnex.getMessage());
			log.debug(ifnex.getMessage(), ifnex);
		}
		
		return destination;
		
	}
	
	/**
	 * {@inheritDoc} Expects the <i>title</i> argument to be of type {@link Series}.
	 * @throws UnsupportedOperationException if <i>title</i> is not a {@link Series}-object.
	 */
	@Override
	public String getRenamedBasename(File file, Title title) throws RenamingException {
		
		RenameFormatModel renameFormatModel = Application.getInstance().getRenameFormatModel();
		
		String currentRenameFormat = renameFormatModel.getRenameFormat();
		String currentDefaultTitleRenameFormat = renameFormatModel.getDefaultTitleRenameFormat();
		
		InterpolationResults interpolationResults = getRenamedFilename(file, title, currentRenameFormat, currentDefaultTitleRenameFormat);
		return interpolationResults.getOutput();
		
	}
	
	public InterpolationResults getRenamedFilename(File file, Title title, String renameFormat, String defaultTitleRenameFormat) throws RenamingException {
		
		if(!(title instanceof Series)){
			Exception rootCause = new UnsupportedOperationException("Expected an object of type Series"); // TODO: rephrase for UI
			throw new RenamingException(rootCause.getMessage(), rootCause);
		}
		
		Series series = (Series) title;
		
		FilenameInfo filenameInfo = filenameAnalyzer.getFilenameInfo(file);
		SeasonEpisodeNumber seasonEpisodeNumber = filenameInfo.getSeasonEpisodeNumber();
		
		// TODO: code duplication against Application.hasValidMapping
		if(seasonEpisodeNumber == null){
			
			throw new NoEpisodeMappingException("Found no season/episode number for file " + file.getName());
			
		} else {
			
			//Application.getInstance().log("Mapped %s to S%02dE%02d", file.getName(), seasonEpisodeNumber.getSeasonNumber(), seasonEpisodeNumber.getEpisodeNumber());
			
			if(series.hasSeasonEpisode(seasonEpisodeNumber)){
				
				// no action needed
				
			} else {
				throw new NoSuchEpisodeException("No matching season/episode " + seasonEpisodeNumber + " found in selected title \"" + title.getTitle() + "\"");
			}
			
		}
		
		Season season = series.getSeason(seasonEpisodeNumber.getSeasonNumber());
		Episode episode = season.getEpisode(seasonEpisodeNumber.getEpisodeNumber());
		
		// generate new abstract file path
		
		InterpolationArguments interpolationArgs = new HashMapInterpolationArguments();
		interpolationArgs.add(RenameFormatInterpolator.SERIES_TITLE, series.getTitle());
		interpolationArgs.add(RenameFormatInterpolator.SEASON_NUMBER, seasonEpisodeNumber.getSeasonNumber());
		interpolationArgs.add(RenameFormatInterpolator.EPISODE_NUMBER, seasonEpisodeNumber.getEpisodeNumber());
		interpolationArgs.add(RenameFormatInterpolator.EPISODE_TITLE, episode.getTitle());
		
		InterpolationResults newFilenameNoExtResults;
		
		try {
			
			String applyRenameFormat = renameFormat; // actual rename format to be applied
			if(episode.isTitleDefault() && defaultTitleRenameFormat != null){
				applyRenameFormat = defaultTitleRenameFormat;
			}
			
			newFilenameNoExtResults = stringInterpolator.interpolateExtended(applyRenameFormat, interpolationArgs);
			return newFilenameNoExtResults;
			
		}
		catch(InterpolationException e) {
			throw new RenamingException(e.getMessage(), e);
		}
		
	}
	
}
