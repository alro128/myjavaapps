package app;

import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;
import app.process.AnnieStandAlone;

public class Consumer implements Runnable {

    final static Logger LOG = Logger.getLogger(Consumer.class);
    private BlockingQueue<Path> queue;

    public Consumer(BlockingQueue<Path> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        LOG.info("Consumer RUN " + App.finished);
        while (!App.finished) {
            LOG.debug("Thread: " + Thread.currentThread().getName() + " Queue size: " + queue.size());
            LOG.debug("Thread: " + Thread.currentThread().getName() + " Queue remainingCapacity: " + queue.remainingCapacity());
            try {

                if (!queue.isEmpty()) {
                    Path p = queue.take();
                    LOG.info("Thread: " + Thread.currentThread().getName() + " Path taken: " + p);
                    AnnieStandAlone c2r = new AnnieStandAlone(p);
                    c2r.annotate();
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOG.error("ERROR: "+ e.getMessage());
            }
        }
    }
}