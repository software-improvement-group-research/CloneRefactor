package com.simonbaars.clonerefactor.metrics;

import com.simonbaars.clonerefactor.datatype.CountMap;

public class Metrics {
	public int totalAmountOfLines = 0;
	public int totalAmountOfNodes = 0;
	public int totalAmountOfTokens = 0;
	
	public int amountOfLinesCloned = 0;
	public int amountOfNodesCloned = 0;
	public int amountOfTokensCloned = 0;
	
	public final CountMap<Integer> amountPerCloneClassSize = new CountMap<>();
	public final CountMap<NodeLocation> amountPerLocation = new CountMap<>();
	public final CountMap<Integer> amountPerNodes = new CountMap<>();
	public final CountMap<Integer> amountPerTotalVolume = new CountMap<>();
	
	@Override
	public String toString() {
		return "Metrics [totalAmountOfLines=" + totalAmountOfLines + ", totalAmountOfNodes=" + totalAmountOfNodes
				+ ", totalAmountOfTokens=" + totalAmountOfTokens + ", amountOfLinesCloned=" + amountOfLinesCloned
				+ ", amountOfNodesCloned=" + amountOfNodesCloned + ", amountOfTokensCloned=" + amountOfTokensCloned
				+ ", amountPerCloneClassSize=" + amountPerCloneClassSize + ", amountPerLocation=" + amountPerLocation
				+ "]";
	}
	
	
	public void add(Metrics metrics) {
		totalAmountOfLines+=metrics.totalAmountOfLines;
		totalAmountOfNodes+=metrics.totalAmountOfNodes;
		totalAmountOfTokens+=metrics.totalAmountOfTokens;
		
		amountOfLinesCloned+=metrics.amountOfLinesCloned;
		amountOfNodesCloned+=metrics.amountOfNodesCloned;
		amountOfTokensCloned+=metrics.amountOfTokensCloned;
		
		amountPerCloneClassSize.addAll(metrics.amountPerCloneClassSize);
		amountPerLocation.addAll(metrics.amountPerLocation);
	}

	
	
	
}
