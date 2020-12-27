package com.plugin.swan;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;
import org.parboiled.errors.ErrorUtils;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.search.QParser;
import org.apache.lucene.search.Query;
import org.apache.solr.request.SolrQueryRequest;

public class SwanQParser extends QParser {

  public SwanQParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
		super(qstr, localParams, params, req);
	}	
	
	@Override
	public Query parse() {

		SwanParser parser = Parboiled.createParser(SwanParser.class);

		ParsingResult<?> result = new RecoveringParseRunner<SpanQuery>(parser.Query()).run(this.qstr);

		if (!result.parseErrors.isEmpty()){
			System.out.println(ErrorUtils.printParseError(result.parseErrors.get(0)));
            return null;
		}

		SpanQuery query = (SpanQuery) result.parseTreeRoot.getValue();
		
		return query;
	}

}
