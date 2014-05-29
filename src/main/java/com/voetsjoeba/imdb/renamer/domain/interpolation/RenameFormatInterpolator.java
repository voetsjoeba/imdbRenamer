package com.voetsjoeba.imdb.renamer.domain.interpolation;

import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.voetsjoeba.imdb.renamer.domain.exception.IllegalInterpolationArgumentsException;
import com.voetsjoeba.imdb.renamer.domain.exception.IllegalInterpolationValueException;
import com.voetsjoeba.imdb.renamer.domain.exception.InfiniteInterpolationException;
import com.voetsjoeba.imdb.renamer.domain.exception.InterpolationException;

/**
 * String interpolator for file rename formats.
 * 
 * @author Jeroen De Ridder
 */
public class RenameFormatInterpolator implements StringInterpolator {
	
	/**
	 * Maximum display length for a number. Used to make sure the user doesn't pass %200000d as a format value and 
	 * stall the GUI.
	 */
	private static final int MAX_NUM_DISPLAY_WIDTH = 20;
	
	public static final String SERIES_TITLE = "seriesTitle";
	public static final String SEASON_NUMBER = "seasonNr";
	public static final String EPISODE_NUMBER = "episodeNr";
	public static final String EPISODE_TITLE = "episodeTitle";
	
	protected enum VarType { STRING, NUM };
	
	// TODO: find a way to avoid code duplication
	protected static final Map<String, VarType> vars;
	
	private static final Map<String, Pattern> patterns;
	static {
		
		vars = new HashMap<String, VarType>();
		vars.put(SERIES_TITLE, VarType.STRING);
		vars.put(SEASON_NUMBER, VarType.NUM);
		vars.put(EPISODE_NUMBER, VarType.NUM);
		vars.put(EPISODE_TITLE, VarType.STRING);
		
		patterns = new HashMap<String, Pattern>();
		for(String varName : vars.keySet()) patterns.put(varName, getVarPattern(varName));
		
	}
	
	@Override
	public InterpolationResults interpolateExtended(String format, InterpolationArguments arguments) throws InterpolationException {
		
		// maintain a mapping of character positions in each intermediate result to character positions in the original
		// input string. each position either holds an Integer indicating its original position within the input string, 
		// or a reference to a VariableMatch object indicating which match caused it to be created
		
		Object[] originalPositions = new Integer[format.length()];
		for(int i=0; i<originalPositions.length; i++) originalPositions[i] = Integer.valueOf(i);
		
		InterpolationResults result = new InterpolationResults();
		result.setInput(format);
		
		// perform interpolation
		String intermediateResult = format;
		
		while(true){
			
			int matchesFound = 0;
			
			for(String varName : vars.keySet()){
				
				// --------------------------------------------------------------------------------------------------------
				
				Matcher matcher = patterns.get(varName).matcher(intermediateResult);
				if(!matcher.find()) continue;
				
				matchesFound++;
				
				// match positions in the previous intermediate (!) result
				int matchStart = matcher.start();
				int matchEnd = matcher.end();
				
				Object substitutionObject = arguments.getValue(varName);
				if(substitutionObject == null) throw new InterpolationException("No such argument: \"" + varName + "\"");
				String substitutionString = substitutionObject.toString(); // default value; used if not overwritten
				
				if(matcher.groupCount() >= 1){
					
					String varArgs = matcher.group(1); // arguments to the variable
					if(varArgs != null){
						substitutionString = applyArguments(varName, varArgs.substring(1), substitutionObject); // .substring(1) to remove the initial ":"
					}
					
				}
				
				// check if the processed substitution string contains the substituted pattern, 
				// which would cause an infinite renaming loop
				Matcher substitutionMatcher = patterns.get(varName).matcher(substitutionString);
				if(substitutionMatcher.find()){
					throw new InfiniteInterpolationException("Cannot substitute " + substitutionObject.toString() + " for variable " + varName + "; new value contains pattern (would cause infinite substitution loop)");
				}
				
				// remember: input coordinates need to be calculated against the original input string, not the intermediate processed ones!
				intermediateResult = replaceSubstring(intermediateResult, matchStart, matchEnd, substitutionString);
				
				// --------------------------------------------------------------------------------------------------------
				
				// indices of the input start and end positions in the originalPositions array
				int inputStartIndex = matchStart;
				int inputEndIndex = matchEnd;
				
				// expand to the left and right
				while(inputStartIndex > 0 && originalPositions[inputStartIndex] instanceof VariableMatch) inputStartIndex--;
				while(inputEndIndex < originalPositions.length - 1 && originalPositions[inputEndIndex-1] instanceof VariableMatch) inputEndIndex++;
				
				assert originalPositions[inputStartIndex] instanceof Integer : "Encountered a non-Integer after expanding inputStartIndex to the left";
				assert (inputEndIndex <= originalPositions.length - 1 && originalPositions[inputEndIndex-1] instanceof Integer) || (inputEndIndex == originalPositions.length) : "Encountered a non-Integer after expanding inputEndIndex to the right";
				
				int inputStart = (Integer) originalPositions[inputStartIndex];
				int inputEnd = (Integer) originalPositions[inputEndIndex - 1] + 1; // inputEndIndex could be the end of the string, so take the one before and increment it by one
				String inputString = format.substring(inputStart, inputEnd); // input string in the original format string
				
				// TODO: check if there is another match that is entirely contained within this match; if so, remove it from the results 
				
				// --------------------------------------------------------------------------------------------------------
				
				// output indices in the intermediary string
				int outputStart = matchStart;
				int outputEnd = matchStart + substitutionString.length();
				
				StringMatchCoordinates inputCoords = new StringMatchCoordinates(inputStart, inputEnd);
				VariableMatch variableMatch = new VariableMatch(varName, inputString, inputCoords, substitutionString);
				
				// --------------------------------------------------------------------------------------------------------
				
				// create new positions array, then replace the old one
				Object[] newPositions = new Object[intermediateResult.length()];
				System.arraycopy(originalPositions, 0, newPositions, 0, matchStart); // copy from the start up to but not include the character at position matchStart
				for(int i=outputStart; i < outputEnd; i++) newPositions[i] = variableMatch;
				System.arraycopy(originalPositions, matchEnd, newPositions, outputEnd, intermediateResult.length() - outputEnd);
				
				originalPositions = newPositions;
				result.addVariableMatch(variableMatch);
				
			}
			
			if(matchesFound == 0) break; // no more matches found, stop looking
			
		}
		
		// register interpolation result and return extended infoz
		result.setOutput(intermediateResult);
		
		return result;
		
	}
	
	@Override
	public String interpolate(String format, InterpolationArguments arguments) throws InterpolationException {
		InterpolationResults extendedResults = interpolateExtended(format, arguments);
		return extendedResults.getOutput();
	}
	
	/**
	 * In <i>subject</i>, replaces the substring at start index <i>substringStartIndex</i> up to but not including <i>substringEndIndex</i> with <i>replacement</i>.
	 * 
	 * @param subject the string to replace the substring of, may be null
	 * @param substringStartIndex the start index of the substring to be replaced
	 * @param substringEndIndex the end index of the substring to be replaced (exclusive)
	 * @param replacement the replacement string
	 */
	protected static String replaceSubstring(String subject, int substringStartIndex, int substringEndIndex, String replacement){
		return StringUtils.overlay(subject, replacement, substringStartIndex, substringEndIndex);
	}
	
	protected static Pattern getVarPattern(String varName){
		
		VarType varType = vars.get(varName);
		String varPattern = null;
		
		switch(varType){
			
			case STRING:
				varPattern = "\\$\\{" + varName + "\\}";
				break;
				
			case NUM:
				varPattern = "\\$\\{" + varName + "(:0?[1-9][0-9]*)?\\}";
				break;
				
			default:
				break;
				
		}
		
		if(varPattern == null) return null;
		return Pattern.compile(varPattern);
		
	}
	
	/**
	 * Processes the value <i>substitutionObject</i> for the variable <i>varName</i> according to arguments <i>varArgs</i>.
	 * 
	 * @param varName the variable name for which <i>substitutionObject</i> is the value
	 * @param varArgs the arguments to the variable
	 * @param substitutionObject the unprocessed value for the variable to be processed according to the arguments provided
	 * @throws IllegalInterpolationArgumentsException if the supplied arguments are invalid
	 * @throws IllegalInterpolationValueException if the substituionObject is not a valid object for this argument
	 */
	protected String applyArguments(String varName, String varArgs, Object substitutionObject) throws IllegalInterpolationArgumentsException, IllegalInterpolationValueException{
		
		String result = substitutionObject.toString();
		
		VarType varType = vars.get(varName);
		switch(varType){
			
			case NUM:
				
				// ---------------------------------------------------------------------------------------------
				// make sure the doesn't stall the GUI by specifying a value that is too large
				
				Integer displayWidth = null;
				try {
					displayWidth = Integer.parseInt(StringUtils.stripStart(varArgs, "0"));
					if(displayWidth > MAX_NUM_DISPLAY_WIDTH) throw new NumberFormatException("Display length too large"); // just to trigger the catch case below
				}
				catch(NumberFormatException nfex){
					// happens on integer overflow or invalid number
					throw new IllegalInterpolationArgumentsException("Illegal argument \"" + varArgs + "\" to variable " + varName + "; display length too large.", nfex);
				}
				
				// ---------------------------------------------------------------------------------------------
				
				try {
					String formatString = "%" + varArgs + "d";
					result = String.format(formatString, substitutionObject);
				}
				catch(NumberFormatException nfex){
					// this really shouldn't happen; this would mean we passed a non-integer object
					throw new IllegalInterpolationValueException("Illegal value type for variable \"" + varName + "\"; not a numeric value", nfex);
				}
				catch(IllegalFormatException ifex){
					throw new IllegalInterpolationArgumentsException("Illegal arguments \"" + varArgs + "\" to variable " + varName);
				}
				
				break;
				
			default:
				break;
				
		}
		
		return result;
		
	}
	
}
