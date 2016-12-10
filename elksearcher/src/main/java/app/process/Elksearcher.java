package app.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;

import app.App;
import app.helper.PropertiesCache;

public class Elksearcher {

    final static Logger LOG = Logger.getLogger(Elksearcher.class);
    Path filepath;
    final String ELKHOST = PropertiesCache.getInstance().getProperty("elasticsearch.hostname");
    final int ELKPORT = Integer.parseInt(PropertiesCache.getInstance().getProperty("elasticsearch.port"));
    final String CLUSTER = PropertiesCache.getInstance().getProperty("elasticsearch.cluster");

    public Elksearcher(Path filepath) {
        this.filepath = filepath;
    }

    public void search() {

        final String index = PropertiesCache.getInstance().getProperty("elasticsearch.index");
        final String type = PropertiesCache.getInstance().getProperty("elasticsearch.type");
        final String DELIMITER = PropertiesCache.getInstance().getProperty("csv.delimeter");
        String fileinput = filepath.toString();

        LOG.info("searching " + fileinput);
        try {
            String fileoutput = FilenameUtils.getBaseName(fileinput) + ".uri.csv";

            Settings settings = ImmutableSettings.settingsBuilder()
                    .put("cluster.name", CLUSTER).build();
            Client client = new TransportClient(settings);
            ((TransportClient) client).addTransportAddress(new InetSocketTransportAddress(ELKHOST, ELKPORT));
           
            HashSet<String> csvset = new HashSet<String>();
            String currentLine = null;
            BufferedReader br = new BufferedReader(new FileReader(fileinput));
            while ((currentLine = br.readLine()) != null) {
                String str[] = currentLine.split(DELIMITER);
                String label = str[1];
                
                SearchRequestBuilder builder = client
                        .prepareSearch(index)
                        .setTypes(type)
                        .setQuery(QueryBuilders.matchPhraseQuery("aliases", label));
                LOG.debug("SearchRequestBuilder: " + builder.toString());
                SearchResponse searchResponse = builder.execute().actionGet();

                if (searchResponse.getHits().getTotalHits() > 0) {
                    Map<String, Object> source = searchResponse.getHits().getAt(0).getSource();
                    String uri = (String) source.get("id");
                    csvset.add(currentLine + uri);
                    LOG.debug("LABEL: " + label);
                    LOG.debug("URI: " + uri);
                } else {
                    csvset.add(currentLine);
                }
            }
            br.close();
            client.close();

            StringBuilder csvresponse = new StringBuilder();
            for (String row : csvset) {
                    csvresponse.append(row + "\n");
            }
            
            FileWriter writer = new FileWriter(App.DIR_OUTPUT + File.separator + fileoutput);
            writer.write(csvresponse.toString());
            LOG.info("file created: " + fileoutput);
            writer.close();

        } catch (Exception ex) {
            LOG.error("ERROR: " + fileinput + " : " + ex.getMessage());
        }
    }

}
