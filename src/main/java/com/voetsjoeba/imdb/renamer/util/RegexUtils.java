package com.voetsjoeba.imdb.renamer.util;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
	
	/**
	 * Returns the last match of <tt>pattern</tt> in <tt>subject</tt> as a MatchResult instance, or null if the pattern
	 * doesn't match.
	 */
	public static MatchResult getLastMatch(final String subject, final Pattern pattern){
		
		MatchResult matchResult = null;
		Matcher matcher = pattern.matcher(subject);
		
		// we're interested in the last match
		while(matcher.find()) matchResult = matcher.toMatchResult();
		return matchResult;
		
	}
	
}
