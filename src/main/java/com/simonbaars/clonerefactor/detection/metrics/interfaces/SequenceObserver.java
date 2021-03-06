package com.simonbaars.clonerefactor.detection.metrics.interfaces;

import com.simonbaars.clonerefactor.detection.metrics.ProblemType;
import com.simonbaars.clonerefactor.detection.model.Sequence;

public interface SequenceObserver {
	public void update(ProblemType problem, Sequence sequence, int problemSize);
}
