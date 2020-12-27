package com.plugin.swan;

import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanNearQuery;

public class SwanQueries {
  
	static int MAX_PARAGRAPH_LENGTH = 5000;
	static int MAX_SENTENCE_LENGTH = 500;
	
	public static SpanQuery SAME(SpanQuery left,SpanQuery right) {
		return new SpanNearQuery(
			new SpanQuery[] { left, right },	MAX_PARAGRAPH_LENGTH, false);
	}
	
	public static SpanQuery WITH(SpanQuery left,SpanQuery right) {
		return new SpanNearQuery(
			new SpanQuery[] { left, right },	MAX_SENTENCE_LENGTH, false);
	}
	
	public static SpanQuery ADJ(SpanQuery left,SpanQuery right) {
		return new SpanNearQuery(
			new SpanQuery[] { left, right },	1, true);
	}
	
	public static SpanQuery NEAR(SpanQuery left,SpanQuery right) {
		return new SpanNearQuery(
			new SpanQuery[] { left, right },	1, false);
	}
}