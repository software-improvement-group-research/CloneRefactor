package com.simonbaars.clonerefactor.detection.interfaces;

import java.util.List;
import java.util.stream.IntStream;

import com.simonbaars.clonerefactor.detection.type2.model.WeightedPercentage;

public interface CalculatesPercentages {
	public default double calcPercentage(int part, int whole) {
		return whole == 0 ? 0D : (double)part/(double)whole*100D;
	}
	
	public default double diffPerc(int[] arr1, int[] arr2) {
		int same = 0, diff = 0;
		for(int i = 0; i<arr1.length; i++){
			if(arr1[i] == arr2[i]) same++; 
			else diff++;
		}
		return calcPercentage(diff, same+diff);
	}
	
	public default double diffPerc(int[][] arr) {
		return diffPerc(arr, IntStream.range(0, arr.length).toArray());
	}
	
	public default double diffPerc(int[][] arr, int[] relevantIndices) {
		int same = 0, diff = 0;
		for(int i = 0; i<relevantIndices.length; i++) {
			for(int j = i+1; j<relevantIndices.length; j++) {
				for(int k = 0; k<arr[relevantIndices[i]].length; k++){
					if(arr[relevantIndices[i]][k] == arr[relevantIndices[j]][k]) same++; 
					else diff++;
				}
			}
		}
		return calcPercentage(diff, same+diff);
	}

	public default double calcAvg(List<WeightedPercentage> percentages) {
		if(percentages.isEmpty())
			return 0;
		WeightedPercentage p = percentages.get(0);
		for(int i = 1; i<percentages.size(); i++) {
			p.mergeWith(percentages.get(i));
		}
		return p.getPercentage();
	}
	
	public default int round(double d) {
		return Math.toIntExact(Math.round(d));
	}
}
