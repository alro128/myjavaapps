package app.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class PropertiesCache {

    private static final Logger LOG = LogManager.getLogger(PropertiesCache.class);

    private final Properties prop = new Properties();
    final static String PROPFILE = "app.properties";

    private PropertiesCache() {
        // Private constructor to restrict new instances
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(PROPFILE);
        LOG.info("Read all properties from file");
        try {
            prop.load(in);
        } catch (IOException e) {
            LOG.error("ERROR IOException: " + e.getMessage());
        }
    }

    private static class SingletonProps {
        private static final PropertiesCache INSTANCE = new PropertiesCache();
    }

    public static PropertiesCache getInstance() {
        return SingletonProps.INSTANCE;
    }

    public String getProperty(String key) {
        return prop.getProperty(key);
    }

    public boolean containsKey(String key) {
        return prop.containsKey(key);
    }
}