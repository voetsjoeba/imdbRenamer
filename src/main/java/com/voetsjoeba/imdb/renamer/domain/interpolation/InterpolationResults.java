package com.voetsjoeba.imdb.renamer.domain.interpolation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds extended information about an interpolation operation.
 * 
 * @see ExtendedStringInterpolator
 * @author Jeroen De Ridder
 */
public class InterpolationResults {
	
	private String input;
	private String output;
	
	private List<VariableMatch> variableMatches;
	
	public InterpolationResults() {
		variableMatches = new ArrayList<VariableMatch>();
	}
	
	public String getInput() {
		return input;
	}
	
	public String getOutput() {
		return output;
	}
	
	public List<VariableMatch> getVariableMatches() {
		return Collections.unmodifiableList(variableMatches);
	}
	
	public void addVariableMatch(VariableMatch variableMatch){
		variableMatches.add(variableMatch); // TODO: variableMatches should contain only unique variable matches
	}
	
	public void removeVariableMatch(VariableMatch variableMatch){
		variableMatches.remove(variableMatch);
	}
	
	public void setInput(String input) {
		this.input = input;
	}
	
	public void setOutput(String output) {
		this.output = output;
	}
	
}
