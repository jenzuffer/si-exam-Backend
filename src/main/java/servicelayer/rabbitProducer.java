package servicelayer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class rabbitProducer {

    private final static String QUEUE_NAME = "queue-name";
    private static String hostname = "localhost";

    public static void main(String[] args) throws Exception
    {
        String message = "Message send from si-exam-backend rabbit producer to " +
                "python rabbit consumer";
        createQueueSendMessage(message);
        System.out.println("sent message: " + message);
    }
    /*
    queueDeclare Parameters:
    queue - the name of the queue
    durable - true if we are declaring a durable queue (the queue will survive a server restart)
    exclusive - true if we are declaring an exclusive queue (restricted to this connection)
    autoDelete - true if we are declaring an autodelete queue (server will delete it when no longer in use)
    arguments - other properties (construction arguments) for the queue
     */

    public static void createQueueSendMessage(String message) throws Exception
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(hostname);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel())
        {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        }
    }
}
