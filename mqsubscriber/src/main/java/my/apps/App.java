package my.apps;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;

public class App {

    private static final Logger LOG = Logger.getLogger(App.class);

    private App() {
    }

    public static void main(String[] args) {
        Properties prop = new Properties();
        InputStream input = null;

        if (args.length < 1) {
            LOG.error("Usage: SubscriberMQ [application.properties] [queue-name]");
            System.exit(1);
        }

        try {
            input = new FileInputStream(args[0]);
            prop.load(input);
        } catch (IOException e) {
            LOG.error(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOG.error(e);
                }
            }
        }

        try {
            String queueName = args[1];

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost((String) prop.get("service.host"));
            final Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            boolean durable = false;
            channel.queueDeclare(queueName, durable, false, false, null);
            LOG.info(" [*] Waiting for messages. To exit press CTRL+C");

            int prefetchCount = 1;
            channel.basicQos(prefetchCount);

            final Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                        Envelope envelope, AMQP.BasicProperties properties,
                        byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");

                    LOG.info(" [x] Received '" + message + "'");
                    try {
                        LOG.info(" ...working on");
                    } finally {
                        LOG.info(" [x] Done");
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };

            channel.basicConsume(queueName, false, consumer);

        } catch (IOException e) {
            LOG.error(e);
        } catch (TimeoutException e) {
            LOG.error(e);
        } catch (ShutdownSignalException e) {
            LOG.error(e);
        } catch (ConsumerCancelledException e) {
            LOG.error(e);
        }

    }
}