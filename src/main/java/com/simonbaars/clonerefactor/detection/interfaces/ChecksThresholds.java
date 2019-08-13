package com.simonbaars.clonerefactor.detection.interfaces;

import com.simonbaars.clonerefactor.detection.model.Sequence;
import com.simonbaars.clonerefactor.settings.Settings;

public interface ChecksThresholds {
	public default boolean checkThresholds(Sequence s) {
		return compareNodesThreshold(s) && compareLinesThreshold(s) && compareTokensThreshold(s);
	}

	public default boolean compareNodesThreshold(Sequence s) {
		return s.getAny().getAmountOfNodes() >= Settings.get().getMinAmountOfNodes();
	}
	
	public default boolean compareLinesThreshold(Sequence s) {
		return s.getAny().getAmountOfLines() >= Settings.get().getMinAmountOfLines();
	}
	
	public default boolean compareTokensThreshold(Sequence s) {
		return s.getAny().getAmountOfTokens() >= Settings.get().getMinAmountOfTokens();
	}
	
	public default boolean checkType2VariabilityThreshold(double perc) {
		return perc <= Settings.get().getType2VariabilityPercentage();
	}
}
