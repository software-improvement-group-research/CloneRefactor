package com.simonbaars.clonerefactor.model;

import java.io.File;
import java.util.Optional;

public class Location {
	private final File file;
	private int line;
	private int endLine;
	private int amountOfLines = 1;
	
	private Location prevLine;
	private Location clone;
	
	public Location(File file, int line) {
		super();
		this.file = file;
		this.line = line;
		this.endLine = line;
	}

	public File getFile() {
		return file;
	}

	public int getLine() {
		return line;
	}

	public int getBeginLine() {
		return line;
	}
	
	public Location getPrevLine() {
		return prevLine;
	}

	public void setPrevLine(Location nextLine) {
		this.prevLine = nextLine;
	}

	public Location getClone() {
		return clone;
	}

	public void setClone(Location clone) {
		this.clone = clone;
	}

	@Override
	public String toString() {
		return "Location [file=" + file + ", line=" + line + "]";
	}
	
	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}
	
	public int getEndLine() {
		return endLine;
	}
	
	public int lines() {
		return getEndLine() - getBeginLine() + 1;
	}

	public void setBeginLine(int beginLine) {
		this.line = beginLine;
	}

	public int getAmountOfLines() {
		return amountOfLines;
	}

	public void setAmountOfLines(int amountOfLines) {
		this.amountOfLines = amountOfLines;
	}
	
}
