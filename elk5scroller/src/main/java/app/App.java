package app;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import java.io.File;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import app.helper.PropertiesCache;

public class App {
    
    private static final Logger LOG = LogManager.getLogger(App.class);
    final static String ENCODING = "UTF-8";

    public static void main(String[] args) {

        String index = PropertiesCache.getInstance().getProperty("elasticsearch.index");

        if (args.length == 0 || args.length > 1) {
            LOG.info("Proper Usage is: java -jar elk5scroller.jar index");
            LOG.info("index: elasticsearch index");
            System.exit(0);
        } else if (args.length == 1) {
            // elasticsearch.index
            index = args[0];
        }

        String elkhost = PropertiesCache.getInstance().getProperty("elasticsearch.hostname");
        int elkport = Integer.parseInt(PropertiesCache.getInstance().getProperty("elasticsearch.port"));

        String idfield = PropertiesCache.getInstance().getProperty("index.id");
        int scrolltime = Integer.parseInt(PropertiesCache.getInstance().getProperty("scroll.time"));
        int scrollsize = Integer.parseInt(PropertiesCache.getInstance().getProperty("scroll.size"));
        String fileoutformat = PropertiesCache.getInstance().getProperty("file.out");

        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = null;
        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elkhost), elkport));

            // old versions
            // TransportClient client = new TransportClient();
            // client.addTransportAddress(new
            // InetSocketTransportAddress(elkhost, elkport));

            boolean indexExists = client.admin().indices().prepareExists(index).execute().actionGet().isExists();
            if (indexExists) {
                LOG.info("Index exists");
            }

            QueryBuilder qb = matchAllQuery();

            SearchResponse scrollResp = client.prepareSearch(index)
                    .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC).setScroll(new TimeValue(scrolltime))
                    .setQuery(qb).setSize(scrollsize).get(); // max of 100 hits will be returned for each scroll

            // old version
            // SearchResponse scrollResp =
            // client.prepareSearch(index).setSearchType(SearchType.SCAN)
            // .setScroll(new
            // TimeValue(scrolltime)).setQuery(qb).setSize(scrollsize).execute().actionGet();

            int c = 0;
            String tid = "";
            PrintWriter out;

            // Scroll until no hits are returned
            do {
                for (SearchHit hit : scrollResp.getHits().getHits()) {
                    c++;
                    tid = (String) hit.getSource().get(idfield);

                    if (tid == null) {
                        tid = (String) hit.getId();
                    }
                    LOG.info(c + ". id:" + tid);

                    try {
                        out = new PrintWriter(new File(tid + fileoutformat), ENCODING);
                        out.println(hit.getSourceAsString());
                        out.close();
                    } catch (Exception e) {
                        LOG.error("File Error " + e.getMessage());
                    }
                }

                scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000))
                        .execute().actionGet();
            } while (scrollResp.getHits().getHits().length != 0);

            client.close();

        } catch (UnknownHostException e1) {
            LOG.error("Unknown Host :" + e1.getMessage());
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }
}
