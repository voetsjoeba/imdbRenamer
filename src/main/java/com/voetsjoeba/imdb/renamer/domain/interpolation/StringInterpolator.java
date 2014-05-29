package com.voetsjoeba.imdb.renamer.domain.interpolation;

import com.voetsjoeba.imdb.renamer.domain.exception.InterpolationException;


/**
 * Interpolates format strings. Variables are entered in the format <pre>${variableName}</pre> and are substituted by
 * their respective values, which are provided by an {@link InterpolationArguments} object.
 * 
 * Modifiers may be applied to variables, which are entered in the form <pre>${variableName:modifierString}</pre>.
 * Currently, only numeric-type variables support modifiers, and only numeric printf-like modifiers are allowed 
 * (i.e. the "02" and "2" in a printf-statement of %02d or %2d, respectively).
 * 
 * @author Jeroen De Ridder
 */
public interface StringInterpolator {
	
	/**
	 * Interpolates the provided format string using the provided interpolation arguments. See the class description
	 * for a detailed discussion of the format string.
	 * @throws InterpolationException if an error occured during interpolation
	 */
	public String interpolate(String format, InterpolationArguments arguments) throws InterpolationException;
	
	/**
	 * Interpolates the provided format sting using the provided arguments, and returns extended information about occurences
	 * of variables in the source and result strings.
	 * @throws InterpolationException if an error occured during interpolation
	 */
	public InterpolationResults interpolateExtended(String format, InterpolationArguments arguments) throws InterpolationException;
	
}
