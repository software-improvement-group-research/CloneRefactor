package com.simonbaars.clonerefactor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.javaparser.JavaToken;
import com.github.javaparser.Range;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithImplements;
import com.simonbaars.clonerefactor.Settings;
import com.simonbaars.clonerefactor.compare.Compare;
import com.simonbaars.clonerefactor.compare.CompareLiteral;
import com.simonbaars.clonerefactor.compare.CompareMethodCall;
import com.simonbaars.clonerefactor.compare.CompareName;
import com.simonbaars.clonerefactor.compare.CompareToken;
import com.simonbaars.clonerefactor.compare.CompareVariable;
import com.simonbaars.clonerefactor.exception.NoTokensException;
import com.simonbaars.clonerefactor.metrics.enums.CloneContents;
import com.simonbaars.clonerefactor.metrics.enums.CloneContents.ContentsType;

public class LocationContents implements FiltersTokens {
	private Range range;
	private final List<Node> nodes;
	private final List<JavaToken> tokens;
	private final List<Compare> compare;
	
	private ContentsType contentsType;
	
	public LocationContents() {
		this.nodes = new ArrayList<>();
		this.tokens = new ArrayList<>();
		this.compare = new ArrayList<>();
	}

	public LocationContents(LocationContents contents) {
		this.range = contents.range;
		this.nodes = new ArrayList<>(contents.getNodes());
		this.tokens = new ArrayList<>(contents.getTokens());
		this.compare = new ArrayList<>(contents.getCompare());
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public int size() {
		return nodes.size();
	}
	
	public void add(Node n) {
		nodes.add(n);
	}
	
	@Override
	public boolean equals (Object o) {
		if(!(o instanceof LocationContents))
			return false;
		LocationContents other = (LocationContents)o;
		//if(other.tokens.equals(tokens) && !IntStream.range(0, compare.size()).allMatch(i -> compare.get(i).compare(other.compare.get(i))))
		//	System.out.println(Arrays.toString(other.compare.toArray())+System.lineSeparator()+Arrays.toString(compare.toArray())+System.lineSeparator()+IntStream.range(0, compare.size()).peek(i -> System.out.println(compare.get(i)+", "+other.compare.get(i)+", "+compare.get(i).compare(other.compare.get(i)))).allMatch(i -> compare.get(i).compare(other.compare.get(i))));
		if(Settings.get().isCompareByTokens()) return tokens.equals(other.tokens);
		return compare.equals(other.compare);
	}
	
	public Map<Range, Node> getNodesForCompare(){
		return getNodesForCompare(getNodes());
	}
	
	public Map<Range, Node> getNodesForCompare(List<Node> parents){
		Map<Range, Node> nodes = new HashMap<>();
		for(Node node : parents) {
			Optional<Range> rangeOptional = node.getRange();
			if(rangeOptional.isPresent()) {
				Range r = rangeOptional.get();
				if(range.contains(r) && (!(node instanceof SimpleName) || !nodes.containsKey(r))) {
					nodes.put(r, node);
				} else if (r.begin.isAfter(range.end))
					return nodes;
			}
			nodes.putAll(getNodesForCompare(node.getChildNodes()));
		}
		return nodes;
	}
	
	public String getEffectiveTokenTypes() {
		return getTokens().stream().map(e -> "["+e.asString()+", "+e.getCategory()+", "+e.getKind()+"]").collect(Collectors.joining(", "));
	}

	public int getAmountOfTokens() {
		return getTokens().size();
	}

	@Override
	public int hashCode() {
		if(Settings.get().isCompareByTokens()) return tokens.hashCode();
		int prime = 31;
		int result = 1;
		for(Compare node : compare) {
			result = prime*result*node.getHashCode();
		}
		return result;
	}

	@Override
	public String toString() {
		return getTokens().stream().map(e -> e.asString()).collect(Collectors.joining());
	}

	public Range addTokens(Node n, TokenRange tokenRange, Range validRange) {
		addTokensInRange(n, tokenRange, validRange);
		if(tokens.isEmpty())
			throw new NoTokensException(n, tokenRange, validRange);
		range = new Range(tokens.get(0).getRange().get().begin, tokens.get(tokens.size()-1).getRange().get().end);
		if(!Settings.get().isCompareByTokens()) createCompareList(n);
		return range; 
	}

	private void createCompareList(Node n) {
		Map<Range, Node> compareMap = getNodesForCompare(Arrays.asList(n));
		getTokens().forEach(token -> {
			Optional<Entry<Range, Node>> thisNodeOptional = compareMap.entrySet().stream().filter(e -> e.getKey().contains(token.getRange().get())).findAny();
			if(thisNodeOptional.isPresent()) {
				Entry<Range, Node> thisNode = thisNodeOptional.get();
				if(thisNode != null){
					Compare createdNode = Compare.create(thisNode.getValue(), token, Settings.get().getCloneType());
					getCompare().add(createdNode);
					if(createdNode instanceof CompareToken)
						compareMap.remove(thisNode.getKey()); 
					else thisNode.setValue(null);
				}
			} else getCompare().add(Compare.create(token, token, Settings.get().getCloneType()));
		});
		//getTokens().forEach(e -> getCompare().add(Compare.create(e.getRange().isPresent() && compareMap.containsKey(e.getRange().get()) ? compareMap.get(e.getRange().get()) : e, e, CloneDetection.type)));
		//System.out.println(getCompare().stream().map(e -> e.toString()).collect(Collectors.joining(", ", "[", "]")));
		//System.out.println(compareMap.values().stream().map(e -> e.toString()+" "+e.getClass().getSimpleName()).collect(Collectors.joining(", ")));
	}

	private void addTokensInRange(Node n, TokenRange tokenRange, Range validRange) {
		for(JavaToken token : tokenRange) {
			Optional<Range> r = token.getRange();
			if(r.isPresent()) {
				if(!validRange.contains(r.get())) break;
				if(isComparableToken(token)) tokens.add(token);
				if(n instanceof NodeWithImplements && token.asString().equals("{")) break; // We cannot exclude the body of class files, this is a workaround.
			}
		}
	}

	public List<JavaToken> getTokens() {
		return tokens;
	}

	public void merge(LocationContents contents) {
		nodes.addAll(contents.getNodes());
		tokens.addAll(contents.getTokens());
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

	public Set<Integer> getEffectiveLines() {
		return getTokens().stream().map(e -> e.getRange().get().begin.line).collect(Collectors.toSet());
	}

	public List<Compare> getCompare() {
		return compare;
	}

	public String toNodeClasses() {
		return getNodesForCompare().values().stream().map(e -> e.toString()+ " => " +e.getClass().getName()).collect(Collectors.joining(", ", "[", "]"));
	}
	
	public ContentsType getContentsType() {
		return contentsType;
	}
	
	public void setMetrics(CloneContents c) {
		this.contentsType = c.get(this);
	}
	
	@SuppressWarnings("rawtypes")
	public final List<Compare> getType2Variable(Class...of) {
		return compare.stream().filter(e -> Arrays.stream(of).anyMatch(f -> f.equals(e.getClass()))).map(e -> (Compare)e).collect(Collectors.toList());
	}

	public List<Compare> getType2Threshold() {
		return getType2Variable(CompareLiteral.class, CompareName.class, CompareMethodCall.class, CompareVariable.class);
	}

	public String compareTypes() {
		return getCompare().stream().map(e -> e.getClass().getName()).collect(Collectors.joining(","));
	}
}
