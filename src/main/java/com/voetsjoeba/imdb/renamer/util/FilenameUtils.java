package com.voetsjoeba.imdb.renamer.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.commons.lang.SystemUtils;

import com.voetsjoeba.imdb.renamer.domain.exception.IllegalWindowsBasenameException;

public class FilenameUtils extends org.apache.commons.io.FilenameUtils {
	
	private static final Collection<String> windowsIllegalBasenames = Arrays.asList(
		"CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"
	);
	
	/**
	 * Removes unsafe characters from a file name.
	 * @throws IllegalWindowsBasenameException if the resulting filename is illegal on the windows platform (eg. a filename like "CON" or "LPT1.txt"). You could remove these from the string,
	 * but if that's the whole filename then you don't have anything left, so the caller needs to deal with this. 
	 */
	public static String safeFilename(String filename) throws IllegalWindowsBasenameException {
		
		String newFilename = filename;
		Pattern p = Pattern.compile("([?\\/:*\"<>|`]|\\p{Cntrl}|[\\t\\n\\x0B\\f\\r])+", Pattern.CASE_INSENSITIVE);
		newFilename = p.matcher(filename).replaceAll("");
		
		if(SystemUtils.IS_OS_WINDOWS){
			// illegal filenames in windows
			String basename = FilenameUtils.getBaseName(newFilename);
			if(windowsIllegalBasenames.contains(basename)) throw new IllegalWindowsBasenameException("Illegal windows basename \"" + basename + "\" from original filename \"" + filename + "\"");
		}
		
		return newFilename;
		
	}
	
}
