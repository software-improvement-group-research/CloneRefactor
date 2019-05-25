package com.simonbaars.clonerefactor.detection.type2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Type2Location {
	private int[] contents;
	private final Map<Type2Location, WeightedPercentage> equalityMap = new HashMap<>();
	private final List<Type2Statement> statementsWithinThreshold = new ArrayList<>();
	
	public Type2Location(int[] contents) {
		super();
		this.contents = contents;
	}

	public int[] getContents() {
		return contents;
	}

	public void setContents(int[] contents) {
		this.contents = contents;
	}

	public Map<Type2Location, WeightedPercentage> getEqualityMap() {
		return equalityMap;
	}

	public List<Type2Statement> getStatementsWithinThreshold() {
		return statementsWithinThreshold;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(contents);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Type2Location other = (Type2Location) obj;
		if (!Arrays.equals(contents, other.contents))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Type2Location [contents=" + Arrays.toString(contents) + "]";
	}
}
