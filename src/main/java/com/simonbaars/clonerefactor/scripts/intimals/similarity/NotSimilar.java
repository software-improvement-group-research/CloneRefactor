package com.simonbaars.clonerefactor.scripts.intimals.similarity;

public class NotSimilar extends Similarity {

	public NotSimilar() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean isMoreImportant(Similarity similarity) {
		return false;
	}

}