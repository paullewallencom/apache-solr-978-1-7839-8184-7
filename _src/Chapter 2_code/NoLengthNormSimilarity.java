package com.myscorer;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.DefaultSimilarity;

public class NoLengthNormSimilarity extends DefaultSimilarity {
	// return field's boost irrespective of the length of the field.
	@Override
	public float lengthNorm(FieldInvertState state) {
		return state.getBoost();	    
	}
}