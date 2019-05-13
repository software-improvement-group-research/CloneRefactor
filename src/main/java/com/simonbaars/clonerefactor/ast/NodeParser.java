package com.simonbaars.clonerefactor.ast;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.simonbaars.clonerefactor.metrics.MetricCollector;
import com.simonbaars.clonerefactor.model.location.Location;
import com.simonbaars.clonerefactor.model.location.LocationContents;

public class NodeParser implements Parser, RequiresNodeOperations {
	final Map<LocationContents, Location> lineReg = new HashMap<>();
	private final MetricCollector metricCollector;
	
	public NodeParser(MetricCollector metricCollector) {
		this.metricCollector = metricCollector;
	}

	public Location extractLinesFromAST(Location prevLocation, CompilationUnit cu, Node n) {
		if(n instanceof ImportDeclaration || n instanceof PackageDeclaration || isExcluded(n))
			return prevLocation;
		if(!(n instanceof CompilationUnit || n instanceof BlockStmt || n instanceof LocalClassDeclarationStmt))
			prevLocation = setIfNotNull(prevLocation, parseToken(prevLocation, cu,  n));
		for (Node child : childrenToParse(n)) {
			prevLocation = setIfNotNull(prevLocation, extractLinesFromAST(prevLocation, cu, child));
		}
		return prevLocation;
	}
	
	
	public Location parseToken(Location prevLocation, CompilationUnit cu, Node n) {
		Range range = getActualRange(n);
		Location thisLocation = prevLocation;
		if(range!=null) {
			thisLocation = new Location(cu.getStorage().get().getPath(), prevLocation);
			thisLocation.calculateTokens(n, range);
			if(prevLocation!=null) prevLocation.setNextLine(thisLocation);
			addLineTokensToReg(thisLocation);
		}
		return thisLocation;
	}

	public Location addLineTokensToReg(Location location) {
		if(lineReg.containsKey(location.getContents())) {
			location.setClone(lineReg.get(location.getContents()));
			lineReg.put(location.getContents(), location);
		} else {
			lineReg.put(location.getContents(), location);
		}
		metricCollector.reportFoundNode(location);
		return location;
	}
	
	public Range getActualRange(Node n) {
		Optional<Range> nodeRangeOpt = n.getRange();
		if(nodeRangeOpt.isPresent()) {
			Range nodeRange = nodeRangeOpt.get();
			for(ListIterator<Node> it = n.getChildNodes().listIterator(n.getChildNodes().size()); it.hasPrevious(); ) {
				Node node = it.previous();
				if(!isExcluded(node) && node.getRange().isPresent()) {
					nodeRange = nodeRange.withEnd(node.getRange().get().begin);
				} else break;
			}
			return nodeRange;
		}
		return null;
	}
}
