package app.crawler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import app.helper.PropertiesCache;

public class FileCrawler {

    final static Logger LOG = Logger.getLogger(FileCrawler.class);
    final String FILTEREXTENSION = PropertiesCache.getInstance().getProperty("file.filter.extension");

    public List<Path> getFiles(String in) {

        List<Path> pl = new ArrayList<Path>();
        File f = new File(in);

        if (f.exists()) {
            if (f.isFile()) {
                LOG.debug(in + " exists and is a file");
                pl.add(f.toPath());
            } else if (f.isDirectory()) {
                LOG.debug(in + " exists and is a directory");
                try {
                    Stream<Path> sp = Files.list(f.toPath()).filter(Files::isRegularFile).filter(s -> s.toString().endsWith(FILTEREXTENSION));
                    sp.forEach((p) -> {
                        pl.add(p);
                    });
                    sp.close();
                } catch (IOException e) {
                    LOG.error("ERROR: " + e.getMessage());
                }
            }
        }

        LOG.debug("returning files: " + pl.size());
        return pl;
    }
}
