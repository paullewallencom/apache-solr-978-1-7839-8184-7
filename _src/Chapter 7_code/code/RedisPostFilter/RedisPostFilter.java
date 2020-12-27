package packt.search;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.search.DelegatingCollector;
import org.apache.solr.search.ExtendedQueryBase;
import org.apache.solr.search.PostFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RedisPostFilter extends ExtendedQueryBase implements PostFilter {
	private Set<BytesRef> adsList;
	private Set<String> onlineAds;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public RedisPostFilter() {

		setCache(false);
		try {
			Jedis redisClient = new Jedis("localhost", 6379);
			redisClient.select(1);
			onlineAds = redisClient.smembers("myList");
			this.adsList = new HashSet<BytesRef>(onlineAds.size());
			for (String ad : onlineAds) {
				this.adsList.add(new BytesRef(ad.getBytes()));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean isValid(String adId) throws IOException {
		return this.onlineAds.contains(adId);
	}

	private int count = 0;
	@Override
	public DelegatingCollector getFilterCollector(final IndexSearcher indexSearcher) {
		return new DelegatingCollector() {

			private BinaryDocValues store;

			@Override
			public void setNextReader(AtomicReaderContext context) throws IOException {
				logger.error("count " + ++count);
				this.docBase = context.docBase;
				this.store = FieldCache.DEFAULT.getTerms(context.reader(), "id", false);
				super.setNextReader(context);
			}

			@Override
			public void collect(int docId) throws IOException {
				String id = context.reader().document(docId).get("id");
				if (isValid(id)) {
					super.collect(docId);
				}
			}
		};
	}

	@Override
	public int getCost() {
		return Math.max(super.getCost(), 100);
	}

	@Override
	public boolean getCache() {
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		RedisPostFilter that = (RedisPostFilter) o;

		return !(onlineAds != null ? !onlineAds.equals(that.onlineAds) : that.onlineAds != null);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (onlineAds != null ? onlineAds.hashCode() : 0);
		return result;
	}
}
