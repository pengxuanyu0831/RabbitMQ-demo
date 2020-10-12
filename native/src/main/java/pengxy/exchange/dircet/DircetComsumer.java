package pengxy.exchange.dircet;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DircetComsumer {
    public static final String EXCHANGE_NAME = "dircet_log";

    public static void main(String[] args) throws TimeoutException, IOException {
        // 创建连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 默认端口5672
        connectionFactory.setHost("localhost");
        // 标准写法的创建连接
        Connection connection = connectionFactory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(DircetProduce.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 申明队列要在消费者里面去做
        String queueName = "queue-papap";
        channel.queueDeclare(queueName,false,false,false,null);
        // 绑定 将队列与交换器进行绑定
        final String routeKey = "武田";
        channel.queueBind(queueName,DircetProduce.EXCHANGE_NAME,routeKey);
        System.out.println("waitting for msg....");


        // 声明一个消费者
        final DefaultConsumer consumer  = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                String message = new String(bytes,"UTF-8");
                System.out.println("Received["+envelope.getRoutingKey()+"]"+message);
            }
        };
        channel.basicConsume(queueName,true, consumer);

    }
}
