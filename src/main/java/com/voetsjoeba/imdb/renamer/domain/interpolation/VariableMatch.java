package com.voetsjoeba.imdb.renamer.domain.interpolation;

/**
 * Represents a match of a variable within an interpolation string.
 * 
 * @author Jeroen De Ridder
 */
public class VariableMatch {
	
	protected final String variableName;
	protected final String inputString;
	protected final String outputString;
	protected final StringMatchCoordinates inputCoordinates;
	
	public VariableMatch(String variableName, String inputString, StringMatchCoordinates inputCoordinates, String outputString/*, StringMatchCoordinates outputCoordinates*/) {
		this.variableName = variableName;
		this.inputString = inputString;
		this.outputString = outputString;
		this.inputCoordinates = inputCoordinates;
	}
	
	public String getVariableName() {
		return variableName;
	}
	
	public StringMatchCoordinates getInputCoordinates() {
		return inputCoordinates;
	}
	
	public String getInputString() {
		return inputString;
	}
	
	public String getOutputString() {
		return outputString;
	}
	
	@Override
	public String toString(){
		return "VM("+inputString + " -> " + outputString +")";
	}
	
}
