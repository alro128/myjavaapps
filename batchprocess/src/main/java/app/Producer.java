package app;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class Producer implements Runnable {

    final static Logger LOG = Logger.getLogger(Producer.class);
    private BlockingQueue<Path> queue;
    private List<Path> pl;
    int i = 0;

    public Producer(BlockingQueue<Path> queue, List<Path> pl) {
        this.queue = queue;
        this.pl = pl;
    }

    @Override
    public void run() {

        LOG.debug("Path list size: " + pl.size());
        while (i < pl.size()) {
            LOG.debug("Trying to add : " + i);
            try {
            if (queue.remainingCapacity() > 0) {
                queue.put(pl.get(i));
                LOG.info("Add to queue: item " + i + " : " + pl.get(i));
                i++;
            }
            Thread.sleep(100);
            } catch (InterruptedException e) {
                LOG.error("ERROR: " + e.getMessage());
            }
        }
        App.finished = true;
    }

    // for (int i = 0; i < 8; i++) {
    // System.out.println("Trying to add to queue: String " + i +
    // " and the result was " + queue.offer("String " + i));
    //
    // try {
    // Thread.sleep(500);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // }
    // }
}