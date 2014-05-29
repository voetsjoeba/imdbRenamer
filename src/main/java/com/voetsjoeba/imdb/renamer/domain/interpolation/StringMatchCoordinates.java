package com.voetsjoeba.imdb.renamer.domain.interpolation;

/**
 * Represents match coordinates within a string.
 * 
 * @author Jeroen De Ridder
 */
public class StringMatchCoordinates {
	
	private final int start;
	private final int end;
	
	public StringMatchCoordinates(int start, int end){
		this.start = start;
		this.end = end;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}
	
	@Override
	public String toString(){
		return "[" + start + "," + end + "[";
	}
	
}
