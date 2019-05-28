package com.simonbaars.clonerefactor.detection.type2;

import com.simonbaars.clonerefactor.ast.interfaces.DeterminesNodeTokens;
import com.simonbaars.clonerefactor.model.Sequence;
import com.simonbaars.clonerefactor.model.location.Location;

public class Type2Statement implements DeterminesNodeTokens {
	private final int locationIndex;
	private final int statementIndex;
	private final Type2Contents contents;
	private Type2Statement next;
	private final Type2Statement prev;
	private final Type2Statement mergedWith;
	
	public Type2Statement(int locationIndex, int statementIndex, Type2Contents contents, Type2Statement prev) {
		super();
		this.locationIndex = locationIndex;
		this.statementIndex = statementIndex;
		this.contents = contents;
		this.prev = prev;
		this.mergedWith = null;
	}
	
	public Type2Statement(Type2Statement type2Statement, Type2Statement key) {
		this.locationIndex = type2Statement.locationIndex;
		this.statementIndex = type2Statement.statementIndex;
		this.contents = type2Statement.contents;
		this.prev = type2Statement.prev;
		this.next = type2Statement.next;
		this.mergedWith = key;
	}

	public int getLocationIndex() {
		return locationIndex;
	}
	
	public int getStatementIndex() {
		return statementIndex;
	}
	
	public Type2Contents getContents() {
		return contents;
	}
	
	public Type2Statement getNext() {
		return next;
	}
	
	public void setNext(Type2Statement next) {
		this.next = next;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + locationIndex;
		result = prime * result + statementIndex;
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
		Type2Statement other = (Type2Statement) obj;
		if (locationIndex != other.locationIndex)
			return false;
		return statementIndex == other.statementIndex;
	}

	@Override
	public String toString() {
		return "Type2Statement [locationIndex=" + locationIndex + ", statementIndex=" + statementIndex + ", contents="
				+ contents + "]";
	}

	public Type2Statement getPrev() {
		return prev;
	}

	public Type2Statement mergeWith(Type2Statement key) {
		return new Type2Statement(this, key);
	}

	public int getAmountOfNodes() {
		if(mergedWith == null)
			return 1;
		return mergedWith.getAmountOfNodes() + 1;
	}
	
	public Location convertToLocation(Sequence sequence) {
		return convertToLocation(sequence.getSequence().get(locationIndex));
	}

	private Location convertToLocation(Location location) {
		return new Location(location.getFile(), location.getContents().getNodes().get(statementIndex));
	}
	
	public Type2Statement getLast() {
		if(mergedWith == null)
			return this;
		return mergedWith.getLast();
	}
}
