package com.simonbaars.clonerefactor.refactoring.model;

import com.simonbaars.clonerefactor.ast.MetricObserver;
import com.simonbaars.clonerefactor.metrics.MetricCollector;
import com.simonbaars.clonerefactor.metrics.ProblemType;

public class CombinedMetrics {
	private final int ccIncrease;
	private final int lineSizeIncrease;
	private final int tokenSizeIncrease;
	private final int nodeSizeIncrease;
	private final int unitInterfaceSizeIncrease;
	
	private final int duplicateNodesIncrease;
	private final int duplicateTokensIncrease;
	private final int duplicateLinesIncrease;
	
	public CombinedMetrics(int ccIncrease, int lineSizeIncrease, int tokenSizeIncrease, int nodeSizeIncrease,
			int unitInterfaceSizeIncrease, int nodes, int tokens, int lines) {
		super();
		this.ccIncrease = ccIncrease;
		this.lineSizeIncrease = lineSizeIncrease;
		this.tokenSizeIncrease = tokenSizeIncrease;
		this.nodeSizeIncrease = nodeSizeIncrease;
		this.unitInterfaceSizeIncrease = unitInterfaceSizeIncrease;
		
		this.duplicateNodesIncrease = -nodes;
		this.duplicateTokensIncrease = -tokens;
		this.duplicateLinesIncrease = -lines;
	}

	public int getCcIncrease() {
		return ccIncrease;
	}

	public int getLineSizeIncrease() {
		return lineSizeIncrease;
	}

	public int getTokenSizeIncrease() {
		return tokenSizeIncrease;
	}

	public int getNodeSizeIncrease() {
		return nodeSizeIncrease;
	}

	public int getUnitInterfaceSizeIncrease() {
		return unitInterfaceSizeIncrease;
	}
	
	public String save(MetricCollector collector) {
		collector.getMetrics().incrementGeneralStatistic("CC Increase", ccIncrease);
		collector.getMetrics().incrementGeneralStatistic("Lines Increase", lineSizeIncrease);
		collector.getMetrics().incrementGeneralStatistic("Tokens Increase", tokenSizeIncrease);
		collector.getMetrics().incrementGeneralStatistic("Nodes Increase", nodeSizeIncrease);
		collector.getMetrics().incrementGeneralStatistic("Unit Size Increase", unitInterfaceSizeIncrease);
		return createString(collector);
	}

	public String createString(MetricCollector collector) {
		return "This refactoring has the following effects on system quality metrics:"+System.lineSeparator()+
				tellWhatHappened("Total Cyclomatic Complexity", collector.getMetrics().generalStats.get(MetricObserver.metricTotalSize(ProblemType.UNITCOMPLEXITY)), ccIncrease) +
				tellWhatHappened("Total Unit Interface Size",collector.getMetrics().generalStats.get(MetricObserver.metricTotalSize(ProblemType.UNITINTERFACESIZE)), unitInterfaceSizeIncrease) +
				tellWhatHappened("Total Unit Line Size",collector.getMetrics().generalStats.get(MetricObserver.metricTotalSize(ProblemType.LINEVOLUME)), lineSizeIncrease) +
				tellWhatHappened("Total Unit Token Size",collector.getMetrics().generalStats.get(MetricObserver.metricTotalSize(ProblemType.TOKENVOLUME)), tokenSizeIncrease) +
				tellWhatHappened("Total Nodes",collector.getMetrics().generalStats.get("Total Nodes"), nodeSizeIncrease) + System.lineSeparator() +
				tellWhatHappened("Duplicated Nodes",collector.getMetrics().generalStats.get("Cloned Nodes"), duplicateNodesIncrease) +
				tellWhatHappened("Duplicated Tokens",collector.getMetrics().generalStats.get("Cloned Tokens"), duplicateTokensIncrease) +
				tellWhatHappened("Duplicated Lines",collector.getMetrics().generalStats.get("Cloned Lines"), duplicateLinesIncrease);
	}
	
	private String tellWhatHappened(String metric, int total, int increase) {
		StringBuilder stringBuilder = new StringBuilder(metric+" ");
		if(increase == 0) {
			stringBuilder.append("did not change and is still "+total);
		} else {
			if(increase>0) {
				stringBuilder.append("increased");
			} else {
				stringBuilder.append("decreased");
			}
			stringBuilder.append(" by "+Math.abs(increase)+" from "+total+" to "+(total+increase));
		}
		stringBuilder.append("."+System.lineSeparator());
		return stringBuilder.toString();
	}
}
