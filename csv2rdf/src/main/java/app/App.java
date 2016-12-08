package app;

import org.apache.log4j.Logger;

import app.crawler.FileCrawler;
import app.helper.PropertiesCache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class App {

    final static Logger LOG = Logger.getLogger(App.class);
    final static Integer QUEUE_SIZE = Integer.parseInt(PropertiesCache.getInstance().getProperty("queue.size"));
    final static Integer MAX_WORKERS = Integer.parseInt(PropertiesCache.getInstance().getProperty("batch.workers"));
    final public static String DIR_OUTPUT = PropertiesCache.getInstance().getProperty("dir.output");

    static boolean finished = false;

    public static void main(String[] args) {

        String fileinput = "";

        if (args.length < 1 || args.length > 1) {
            LOG.info("Proper Usage is: java -jar csv2rdf.jar input");
            LOG.info("input: file or directory");
            System.exit(0);
        } else if (args.length == 1) {
            fileinput = args[0];
        }

        FileCrawler fc = new FileCrawler();
        List<Path> fl = fc.getFiles(fileinput);

        fl.forEach((p) -> LOG.info("File: " + p));
        
        try {
            Files.createDirectory(Paths.get(DIR_OUTPUT));
        } catch (IOException e) {
            LOG.error("ERROR: " + e.getMessage());
        }

        BlockingQueue<Path> q = new ArrayBlockingQueue<Path>(QUEUE_SIZE);

        Thread producer = new Thread(new Producer(q, fl));
        producer.start();

        for (int i = 0; i < MAX_WORKERS; i++) {
            Thread consumer = new Thread(new Consumer(q), "t" + i);
            consumer.start();
        }

    }
}
