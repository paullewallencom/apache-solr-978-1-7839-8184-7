package packt.search;

import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.SyntaxError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class RedisQParserPlugin extends QParserPlugin {
	public void init(NamedList args) { /* NOOP */ }
	private Logger logger = LoggerFactory.getLogger(this.getClass());
 
	@Override
	public QParser createParser
			(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
		return new QParser(qstr, localParams, params, req) {
			@Override
			public Query parse() throws SyntaxError {
				logger.info("Redis Post-filter invoked");
				return new RedisPostFilter();
			}
		};
	}
}