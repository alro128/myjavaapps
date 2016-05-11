package app;

import org.apache.log4j.Logger;
import static org.elasticsearch.index.query.QueryBuilders.*;
import org.elasticsearch.search.SearchHit;
import app.helper.PropertiesCache;

import java.io.File;
import java.io.PrintWriter;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;

public class App {

	final static Logger LOG = Logger.getLogger(App.class);
	final static String ENCODING =  "UTF-8";

	public static void main(String[] args) {

		String elkhost = PropertiesCache.getInstance().getProperty("elasticsearch.hostname");
		int elkport = Integer.parseInt(PropertiesCache.getInstance().getProperty("elasticsearch.port"));
		String index = PropertiesCache.getInstance().getProperty("elasticsearch.index");
		String idfield = PropertiesCache.getInstance().getProperty("index.id");
		int scrolltime = Integer.parseInt(PropertiesCache.getInstance().getProperty("scroll.time"));
		int scrollsize = Integer.parseInt(PropertiesCache.getInstance().getProperty("scroll.size"));
		String fileoutformat = PropertiesCache.getInstance().getProperty("file.out");

		TransportClient client = new TransportClient();
		client.addTransportAddress(new InetSocketTransportAddress(elkhost, elkport));

		boolean indexExists = client.admin().indices().prepareExists(index).execute().actionGet().isExists();
		if (indexExists) {
			LOG.info("Index exists");
		}

		QueryBuilder qb = matchAllQuery();
		SearchResponse scrollResp = client.prepareSearch(index).setSearchType(SearchType.SCAN)
				.setScroll(new TimeValue(scrolltime)).setQuery(qb).setSize(scrollsize).execute().actionGet();

		int c = 0;
		String tid = "";
		String ttype = "";
		PrintWriter out;
		while (true) {

			for (SearchHit hit : scrollResp.getHits().getHits()) {
				c++;
				tid = (String) hit.getSource().get(idfield);
				ttype = (String) hit.getType();
				LOG.info(c + ". type:" + ttype + " id:" + tid);

				try {
					out = new PrintWriter(new File(tid + "-" + ttype + fileoutformat), ENCODING);
					out.println(hit.sourceAsString());
					out.close();
				} catch (Exception e) {
					LOG.error("File Error " + e.getMessage());
				}

			}

			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(scrolltime))
					.execute().actionGet();
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}

		}

		client.close();
	}

}