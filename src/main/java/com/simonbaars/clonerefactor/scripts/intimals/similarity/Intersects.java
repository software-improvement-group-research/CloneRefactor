package com.simonbaars.clonerefactor.scripts.intimals.similarity;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.simonbaars.clonerefactor.detection.model.location.Location;
import com.simonbaars.clonerefactor.scripts.intimals.model.PatternLocation;

public class Intersects extends Similarity {
	
	private int unmatchedClone = 0;
	private int unmatchedPattern = 0;
	private int matched = 0;
	
	public Intersects(int unmatchedClone, int unmatchedPattern, int matched) {
		super();
		this.unmatchedClone = unmatchedClone;
		this.unmatchedPattern = unmatchedPattern;
		this.matched = matched;
	}

	public Intersects(PatternLocation pattern, Location clone) {
		Range actualPatternRange = pattern.actualRange();
		Position begin = actualPatternRange.begin.isBefore(clone.getRange().begin) ? actualPatternRange.begin : clone.getRange().begin;
		Position end = actualPatternRange.end.isAfter(clone.getRange().end) ? actualPatternRange.end : clone.getRange().end;
		for(int i = begin.line; i<=end.line; i++) {
			boolean patternIntersects = actualPatternRange.begin.line >= i && actualPatternRange.end.line <= i;
			boolean cloneIntersects = clone.getRange().begin.line >= i && clone.getRange().end.line <= i;
			if(patternIntersects && cloneIntersects) {
				matched++;
			}else if(patternIntersects) {
				unmatchedPattern++;
			} else if(cloneIntersects) {
				unmatchedClone++;
			}
		}
	}

	@Override
	protected boolean isMoreImportant(Similarity similarity) {
		if(!(similarity instanceof Intersects))
			return true;
		Intersects other = (Intersects)similarity;
		return getMatchedMinusUnmatched() > other.getMatchedMinusUnmatched();
	}
	
	private int getMatchedMinusUnmatched() {
		return matched - unmatchedClone - unmatchedPattern;
	}

}