package com.simonbaars.clonerefactor.metrics;

import java.io.File;
import java.util.List;

import com.github.javaparser.JavaToken;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.simonbaars.clonerefactor.datatype.ListMap;
import com.simonbaars.clonerefactor.model.Location;
import com.simonbaars.clonerefactor.model.Sequence;

public class MetricCollector {
	private final ListMap<File, Integer> parsedLines = new ListMap<>();
	private final ListMap<File, Range> parsedTokens = new ListMap<>();
	private final ListMap<File, Range> parsedNodes = new ListMap<>();
	private final Metrics metrics = new Metrics();
	
	public MetricCollector() {}
	
	public void reportFoundNode(Location l) {
		metrics.totalAmountOfLines+=l.getAmountOfLines();
		metrics.totalAmountOfNodes+=l.getAmountOfNodes();
		metrics.totalAmountOfTokens+=l.getAmountOfTokens();
		l.getContents().getNodes().forEach(e -> NodeLocation.registerNode(e));
	}
	
	private int getUnparsedLines(Location l) {
		int amountOfLines = 0;
		for(int i = l.getRange().begin.line; i<=l.getRange().end.line; i++) {
			List<Integer> lines = parsedLines.get(l.getFile());
			if(!lines.contains(i)) {
				amountOfLines++;
				lines.add(i);
			} 
		}
		return amountOfLines;
	}
	
	public Metrics reportClones(List<Sequence> clones) {
		parsedLines.clear();
		for(Sequence clone : clones)
			reportClone(clone);
		NodeLocation.clearClasses();
		return metrics;
	}

	private void reportClone(Sequence clone) {
		metrics.amountPerCloneClassSize.increment(clone.size());
		metrics.amountPerLocation.increment(NodeLocation.getLocation(clone));
		for(Location l : clone.getSequence()) {
			reportClonedLocation(l);
		}
	}

	private void reportClonedLocation(Location l) {
		metrics.amountOfLinesCloned+=getUnparsedLines(l);
		metrics.amountOfTokensCloned+=getUnparsedTokens(l);
		metrics.amountOfNodesCloned+=getUnparsedNodes(l);
	}

	private int getUnparsedTokens(Location l) {
		int amount = 0;
		for(JavaToken n : l.getContents().getTokens()) {
			if(n.getRange().isPresent()) {
				Range r = n.getRange().get();
				if(!parsedTokens.get(l.getFile()).contains(r)) {
					parsedTokens.addTo(l.getFile(), r);
					amount++;
				}
			}
		}
		return amount;
	}

	private int getUnparsedNodes(Location l) {
		int amount = 0;
		for(Node n : l.getContents().getNodes()) {
			Range r = n.getRange().get();
			if(!parsedNodes.get(l.getFile()).contains(r)) {
				parsedNodes.addTo(l.getFile(), r);
				amount++;
			}
		}
		return amount;
	}

	public Metrics getMetrics() {
		return metrics;
	}
}
