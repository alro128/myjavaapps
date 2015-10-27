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
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.ShutdownSignalException;

public class App {

    private static final Logger LOG = Logger.getLogger(App.class);

    private App() {
    }

    public static void main(String[] args) {
        Properties prop = new Properties();
        InputStream input = null;

        if (args.length < 1) {
            LOG.error("Usage: PublishMQ [application.properties] [queue-name] [message]");
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
            String message = args[2];
            
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost((String) prop.get("service.host"));
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            
            boolean durable = false;
            channel.queueDeclare(queueName, durable, false, false, null);

            channel.basicPublish( "", queueName, 
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes());
            
            LOG.info(" [x] Sent '" + queueName + "':'" + message + "'");

            channel.close();
            connection.close();

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
