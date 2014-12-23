package com.voetsjoeba.imdb.renamer.domain.analysis;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voetsjoeba.imdb.domain.SeasonEpisodeNumber;
import com.voetsjoeba.imdb.renamer.event.FilenameAnalysisUpdatedEvent;
import com.voetsjoeba.imdb.renamer.util.RegexUtils;

/**
 * Default FilenameAnalyzer implementation. Applies regular expressions to find common occurences of the season/episode 
 * numbers in file names, and tries to identify the movie/series name. Caches results to prevent slow lookups.
 * 
 * Listens for changes to any of the properties of the FilenameInfo instances created from this analyzer.
 * 
 * @author Jeroen
 */
@ThreadSafe
public class StandardFilenameAnalyzer extends AbstractFilenameAnalyzer implements PropertyChangeListener {
	
	private static final Logger log = LoggerFactory.getLogger(StandardFilenameAnalyzer.class);
	
	// -- patterns -------------------------------------------------------------------------------------------
	
	private static final Pattern videoFormatPattern = Pattern.compile("\\b(m?720[pi]?|m?1080[pi]?|m-?HD)\\b", Pattern.CASE_INSENSITIVE);
	private static final Pattern videoCodecPattern = Pattern.compile("\\b(x264|XviD|DivX)\\b", Pattern.CASE_INSENSITIVE);
	private static final Pattern audioCodecPattern = Pattern.compile("\\b(AAC|MP3|DTS)\\b", Pattern.CASE_INSENSITIVE);
	private static final Pattern sourcePattern = Pattern.compile("\\b(DVDRip|DVDR|DVDScr|BRRip|BDRip|BluRay|PPVRip|TS|R5|CAM|WEBRip|HDTV)\\b", Pattern.CASE_INSENSITIVE);
	//private static final Pattern filesizePattern = Pattern.compile("(?:\\d+\\.)?(\\d+[GM]B)");
	private static final Pattern yearPattern = Pattern.compile("\\b(?:19|20)\\d{2}\\b");
	
	//private static final Pattern yearWordPattern = Pattern.compile("^(?:19|20)\\d\\d$");
	
	private static final Pattern releaseGroupPattern;
	private static final List<Pattern> seasonEpisodeNumberPatterns;
	
	static {
		
		// (?:X) = X as a non-capturing group
		// X?+ = X once, or not at all
		
		// the order of this list was carefully considered -- don't mess with this if you're not sure! See notes below in the extractFilenameInfo method.
		seasonEpisodeNumberPatterns = Arrays.asList(
			Pattern.compile("(?:\\W|_)?+s(\\d{1,2})[-x._]?e?(\\d{1,2})(?:\\W|_)?", Pattern.CASE_INSENSITIVE),
			Pattern.compile("(?:\\W|_)?+(\\d{1,2})[-x._](\\d{2})(?:\\W|_)?", Pattern.CASE_INSENSITIVE),
			Pattern.compile("(?:\\W|_)?+(\\d{1,2})(\\d{2})(?:\\W|_)?", Pattern.CASE_INSENSITIVE) // this is the most generic pattern and hence most likely to produce a wrong match
		);
		
		releaseGroupPattern = Pattern.compile(
			"\\b(DiAMOND|iMMORTALS|SAPPHiRE|VoMiT|PuKKA|SAiNTS|SiNNERS|REWARD|OiC|ORPHEUS|ViSiON|" +
			  "aXXo|MOMENTUM|IMMERSE|FQM|CRiMSON|2HD|RiVER|LOKi|XOR|LOL|NoTV|aAF|MiNT|SYS|CTU|" +
			  "OMiCRON|SFM|STFC|YesTV|CT|CAMERA|SDH|0TV|hV|COCAiN|SiRiUS|TOPAZ|REAVERS|C4TV|DAH|" +
			  "FEVER|ASAP|DIMENSION|FTP|sHoTV|DiVERGE|KYR|P2P|MC8|LulzSec|LOCATiON|BiA|FoV|501st|" +
			  "TCM|UMD|TBS|NBS|ORENJi|MiRAGETV|GOTHiC|TvD|BamHD|442|DeFaCto|m00tv|STFU|HAGGiS|VYS|" +
			  "LATERAL|iNCiTE|iND|iNK|PiX|VSS|WTV|Aph3x|EXT|F5|FAR|fix|FuX|FuX0r|HQTV|HY|JFKXVID|" +
			  "KWYJIBO|mVzTV|NK|Rolex|STi|SWiNE|Tiffy|TVA|UWH|W4F|EP1C|ghost-hd|scOrp|300mbunited.com|" +
			  "RiPPeRS|Walker|" +
			  "team|prototype)\\b" // misc
		);
		
	}
	
	@GuardedBy("fileInfoCache")
	protected final Map<File, FilenameInfo> fileInfoCache; // TODO: this should probably be static and synchronized
	
	// ------------------------------------------------------------------------------------------------------
	
	public StandardFilenameAnalyzer(){
		fileInfoCache = Collections.synchronizedMap(new HashMap<File, FilenameInfo>());
	}
	
	/**
	 * Helper method; does the actual work of extraction information from a file(name). {@link #getFilenameInfo(File)} is 
	 * merely a cache-wrapper around this method.
	 * 
	 * @see #getFilenameInfo(File)
	 */
	protected FilenameInfo extractFilenameInfo(File file) {
		
		String releaseTitle = StringUtils.trimToNull(FilenameUtils.removeExtension(file.getName()));
		if(releaseTitle == null) return null;
		
		FilenameInfo extractedInfo = new FilenameInfo(file);
		
		// ---------------------------------------------------------------------------------------------------
		
		/**
		 * NOTE: should we do the season/episode number extraction first, or the year extraction first? Series filenames
		 * usually carry only a season/episode number indication and not a year indicator, while movies are exactly the 
		 * opposite. So, what do we have at our disposal to identify whether a filename is a movie or a series episode?
		 * It's not always easy to tell for sure, but there are some strong indicators:
		 * 
		 * SERIES:
		 *   - (low) season/episode numbers
		 *   - release group known for releasing series
		 *   - filesize < 700MB or 500MB can be another indicating factor to strengthen a suspicion
		 * 
		 * MOVIES:
		 *   - year indicator
		 *   - release group known for releasing movies
		 * 
		 * Not many series have seasons in the 19/20 range; maybe the only one is the Simpsons or something. So usually 
		 * it shouldn't be a big deal to check for the year first. If the year is in the future from the current date
		 * (let's add a buffer of two years to allow for early releases though), then it has to be a S/E number. Also, 
		 * if the last two digits are in the lower end (like <25), then we should also consider the (albeit rare) 
		 * possibility that this is a S/E number of a long-running series like The Simpsons.
		 */
		
		// try to find a year
		
		// for movies, see if we can find a year somewhere -- if we can, then usually anything that goes in front is part 
		// of the title. movies have been around since roughly the 1920s, so let's search for anything that goes 19xx to 
		// 20xx. Let's search back-to-front to try and make sure we don't accidentally take a number that's part of the
		// movie title as the release year.
		
		/*Integer year = null;
		
		for(int i=titleWords.size() - 1; i >= 0; i--){
			String word = titleWords.get(i);
			if(yearWordPattern.matcher(word).matches()){
				year = Integer.parseInt(titleWords.get(i));
				break;
			}
		}
		
		extractedInfo.setYear(year);*/
		
		Matcher yearMatcher = yearPattern.matcher(releaseTitle);
		MatchResult yearMatchResult = null;
		//String beforeYearSubstring = null; // anything that comes before the year (if found) -- likely to be a movie title
		
		if(yearMatcher.find()){
			
			yearMatchResult = yearMatcher.toMatchResult();
			Integer year = Integer.parseInt(yearMatcher.group());
			
			extractedInfo.setYear(year);
			releaseTitle = removeMatchResult(releaseTitle, yearMatchResult);
			//beforeYearSubstring = releaseTitle.substring(0, yearMatchResult.start());
		}
		
		// -------------------------------------------------------------------------------------------------
		
		// grab the season/episode number
		SeasonEpisodeNumber seNumber = null;
		
		/**
		 * Try to parse the filename with each of the patterns consecutively. We're interested in the earliest match, 
		 * which is kind of problematic when matching these patterns sequentially because an earlier pattern might match 
		 * later in the string and produce a wrong result. Hence, we've ordered the patterns in descending order of 
		 * "speciality", i.e. the most generic pattern is tested last.
		 * 
		 * Regardless though, we should still keep track of all the matches found and use the earliest one; i.e., the one
		 * with the lowest start index.
		 */
		MatchResult earliestMatch = null;
		for(Pattern pattern : seasonEpisodeNumberPatterns){
			
			Matcher matcher = pattern.matcher(releaseTitle);
			if(matcher.find()){
				
				MatchResult matchResult = matcher.toMatchResult();
				if(earliestMatch == null || matchResult.start() < earliestMatch.start()){
					earliestMatch = matchResult;
				}
				
			}
			
		}
		
		if(earliestMatch != null){
			Integer seasonNumber = Integer.valueOf(earliestMatch.group(1));
			Integer episodeNumber = Integer.valueOf(earliestMatch.group(2));
			
			seNumber = new SeasonEpisodeNumber(seasonNumber, episodeNumber);
		}
		
		if(seNumber != null){
			extractedInfo.setSeasonEpisodeNumber(seNumber);
			log.debug("Mapped \"{}\" to {}", file.getName(), seNumber.toString());
		}
		
		// -------------------------------------------------------------------------------------------------
		
		// drop common release title words, like group names, video/audio codecs, rip sources, video formats, ...
		// match various patterns (release group, format, etc.), extract their info and slice it out of the filename
		releaseTitle = extractDropPatterns(releaseTitle, extractedInfo);
		
		// -------------------------------------------------------------------------------------------------
		
		// split the release title by non-word characters
		List<String> titleWords = new ArrayList<String>(Arrays.asList(releaseTitle.split("([^A-Za-z0-9_-])"))); // wrapped in an extra ArrayList because you can't do shit with the list returned by asList (doesn't support removeAll)
		titleWords.removeAll(Collections.singleton("")); // drop all empty entries
		
		extractedInfo.setTitleWords(titleWords);
		log.debug("{}", titleWords);
		
		// -------------------------------------------------------------------------------------------------
		
		return extractedInfo;
		
	}
	
	/**
	 * Helper methods; extracts patterns from the filename that also need to be removed after extraction, and writes
	 * them into a provided FilenameInfo object.
	 * 
	 * @param releaseTitle original filename to extract the patterns from
	 * @return the new filename with all the matched patterns sliced out
	 */
	protected String extractDropPatterns(String releaseTitle, final FilenameInfo filenameInfo) {
		
		MatchResult matchResult = null;
		
		// -- releaseGroup ------------------------------------------------------------------------------
		
		matchResult = RegexUtils.getLastMatch(releaseTitle, releaseGroupPattern);
		if(matchResult != null){
			filenameInfo.setReleaseGroup(matchResult.group());
			releaseTitle = removeMatchResult(releaseTitle, matchResult);
		}
		
		// -- videoCodec ------------------------------------------------------------------------------
		
		matchResult = RegexUtils.getLastMatch(releaseTitle, videoCodecPattern);
		if(matchResult != null){
			filenameInfo.setVideoCodec(matchResult.group());
			releaseTitle = removeMatchResult(releaseTitle, matchResult);
		}
		
		// -- audioCodec ------------------------------------------------------------------------------
		
		matchResult = RegexUtils.getLastMatch(releaseTitle, audioCodecPattern);
		if(matchResult != null){
			filenameInfo.setAudioCodec(matchResult.group());
			releaseTitle = removeMatchResult(releaseTitle, matchResult);
		}
		
		// -- videoFormat ------------------------------------------------------------------------------
		
		matchResult = RegexUtils.getLastMatch(releaseTitle, videoFormatPattern);
		if(matchResult != null){
			filenameInfo.setVideoFormat(matchResult.group());
			releaseTitle = removeMatchResult(releaseTitle, matchResult);
		}
		
		// -- source ----------------------------------------------------------------------------------
		
		matchResult = RegexUtils.getLastMatch(releaseTitle, sourcePattern);
		if(matchResult != null){
			filenameInfo.setSource(matchResult.group());
			releaseTitle = removeMatchResult(releaseTitle, matchResult);
		}
		
		return releaseTitle;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FilenameInfo getFilenameInfo(File file){
		
		FilenameInfo result = null;
		synchronized(fileInfoCache){
			
			result = fileInfoCache.get(file);
			if(result == null){
				
				// no cached result, extract new file info
				result = extractFilenameInfo(file);
				fileInfoCache.put(file, result);
				
				/**
				 * We can only add the property change listener at this point, i.e. when the FilenameInfo object has been 
				 * completed constructed and entered into the cache. The reason is that some of the listeners will request
				 * the FilenameInfo for the entire file list (for instance, to figure out how many of them need updating), 
				 * and will hence request the FilenameInfo that is still being constructed. Because it hasn't been entered
				 * into the cache yet, this same method will call extractFilenameInfo again, resulting in an infinite call
				 * loop and a stack overflow.
				 */
				result.addPropertyChangeListener(this);
				
			}
		}
		
		return result;
		
	}
	
	/**
	 * Removes a regular expression match from its subject string.
	 * 
	 * @param str the subject string to remove the match from
	 * @param matchResult the match to remove
	 * @return the subject string with the matching region removed
	 */
	protected String removeMatchResult(String str, MatchResult matchResult){
		return StringUtils.overlay(str, "", matchResult.start(), matchResult.end());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		assert evt.getSource() instanceof FilenameInfo;
		//FilenameInfo filenameInfo = (FilenameInfo) evt.getSource();
		fireFilenameAnalysisChanged(new FilenameAnalysisUpdatedEvent(this));
		
	}
	
	/*@Override
	public void setSeasonEpisodeNumber(File file, SeasonEpisodeNumber seasonEpisodeNumber) {
		
		if(file == null) throw new IllegalArgumentException("Cannot assert mapping; file must not be null");
		if(seasonEpisodeNumber == null) throw new IllegalArgumentException("Cannot assert mapping: season/episode number must not be null");
		
		fileInfoCache.put(file, seasonEpisodeNumber);
		fireFilenameAnalysisChanged(new FilenameAnalysisUpdatedEvent(this));
		
	}*/
	
	/*@Override
	public boolean removeSeasonEpisodeNumber(File file) {
		
		if(file == null) return false;
		
		boolean result = fileInfoCache.containsKey(file);
		fileInfoCache.remove(file);
		
		fireFilenameAnalysisChanged(new FilenameAnalysisUpdatedEvent(this));
		return result;
		
	}*/
	
}
