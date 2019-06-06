package com.simonbaars.clonerefactor.metrics.enums;

import java.util.List;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.simonbaars.clonerefactor.ast.interfaces.RequiresNodeOperations;
import com.simonbaars.clonerefactor.metrics.enums.CloneRefactorability.Refactorability;
import com.simonbaars.clonerefactor.model.Sequence;
import com.simonbaars.clonerefactor.model.location.Location;

public class CloneRefactorability implements MetricEnum<Refactorability>, RequiresNodeOperations {
	public enum Refactorability{
		CANBEEXTRACTED,
		NOEXTRACTIONBYCONTENTTYPE,
		PARTIALBLOCK,
		COMPLEXCONTROLFLOW,
	}

	@Override
	public Refactorability get(Sequence sequence) {
		if(new CloneContents().get(sequence)!=CloneContents.ContentsType.PARTIALMETHOD)
			return Refactorability.NOEXTRACTIONBYCONTENTTYPE;
		if(sequence.getLocations().stream().anyMatch(e -> e.getContents().getNodes().stream().anyMatch(n -> complexControlFlow(n)))) {
			if(flowEndsInReturnAndContainsOnlyReturnStatements(sequence))
				return Refactorability.COMPLEXCONTROLFLOW;
		}
		for(Location location : sequence.getLocations()) {
			for(Node n : location.getContents().getNodes()) {
				List<Node> children = childrenToParse(n);
				if(children.stream().anyMatch(e -> !isExcluded(e) && !location.getContents().getNodes().contains(e))) {
					return Refactorability.PARTIALBLOCK;
				}
			}
		}
		return Refactorability.CANBEEXTRACTED;
	}
	
	private boolean complexControlFlow(Node n) {
		return n instanceof BreakStmt || n instanceof ReturnStmt || n instanceof ContinueStmt;
	}
	
	private boolean flowEndsInReturnAndContainsOnlyReturnStatements(Sequence sequence) {
		for(Location location : sequence.getLocations()) {
			if(location.getContents().getNodes().stream().filter(n -> complexControlFlow(n)).allMatch(e -> e instanceof ReturnStmt) && location.getContents().getNodes().get(location.getContents().getNodes().size()-1) instanceof ReturnStmt) {
				return false;
			}
		}
		return true;
	}
}
