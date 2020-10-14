package pengxy.exchange.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class FanoutProduce {
    public static final String EXCHANGE_NAME = "fanout_log";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        // 设置MabbitMQ所在主机ip或者主机名
        factory.setHost("127.0.0.1");
        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个信道
        Channel channel = connection.createChannel();
        // 指定转发Fanout模式
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        String[] routeKeys = {"筱田", "桃乃", "武田"};
        for (int i = 0; i < 3; i++) {
            String routeKey = routeKeys[i % 3];
            String msg = "Hello RabbitMQ" + (i + 1);
            channel.basicPublish(EXCHANGE_NAME, routeKey, null, msg.getBytes());
            System.out.println("=====send success==== :" + routeKey + "   " + msg);
        }
        channel.close();
        connection.close();
    }
}
