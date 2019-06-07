package com.simonbaars.clonerefactor.detection.type3;

import java.nio.file.Path;

import com.github.javaparser.Range;
import com.simonbaars.clonerefactor.model.location.Location;
import com.simonbaars.clonerefactor.model.location.LocationContents;

public class Type3Location extends Location implements Type3Calculation{
	private LocationContents diffContents;
	private LocationContents combinedContents;

	public Type3Location(Location clonedLocation, Range r) {
		super(clonedLocation, r);
	}

	public Type3Location(Location clonedLocation) {
		super(clonedLocation);
	}


	public Type3Location(Path file, Range range) {
		super(file, range);
	}

	public Type3Location(Location location, Location location2) {
		super(location.getFile(), location.getRange());
		if(location.getRange().isBefore(location2.getRange().begin))
			mergeLocations(location, location2);
		else mergeLocations(location2, location);
		if(location instanceof Type3Location) {
			diffContents.merge(((Type3Location)location).getDiffContents());
		} 
		if(location2 instanceof Type3Location) {
			diffContents.merge(((Type3Location)location2).getDiffContents());
		}
	}

	private void mergeLocations(Location before, Location after) {
		Range r = before.getRange().withEnd(after.getRange().end);
		setDiffContents(calculateDiffContents(before, after));
		getDiffContents().determineRange();
		populateContents(getContents(), before.getContents());
		populateContents(getContents(), getDiffContents());
		populateContents(getContents(), after.getContents());
		setRange(r);
	}

	public LocationContents getDiffContents() {
		return diffContents;
	}

	public void setDiffContents(LocationContents diffContents) {
		this.diffContents = diffContents;
	}
	
	public LocationContents getCombinedContents() {
		return combinedContents;
	}

	public void setCombinedContents(LocationContents combinedContents) {
		this.combinedContents = combinedContents;
	}
	
	@Override
	public String toString() {
		String s = super.toString();
		return new StringBuilder(s).insert(s.length()-1, ", type3range="+diffContents.getRange()+", type3nodes="+diffContents.size()).toString();
	}
}
