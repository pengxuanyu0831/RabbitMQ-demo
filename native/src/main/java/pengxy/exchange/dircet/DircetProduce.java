package pengxy.exchange.dircet;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DircetProduce {
    public static final String EXCHANGE_NAME = "dircet_log";

    public static void main(String[] args) throws TimeoutException,IOException{
        // 创建连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 默认端口5672
        connectionFactory.setHost("localhost");
        // 标准写法的创建连接
        Connection connection = connectionFactory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 声明路由键
        String[] routeKeys = {"筱田","桃乃","武田"};
        for(int i=0;i<3;i++){
            String routeKey = routeKeys[i%3];
            String msg = "Hello RabbitMQ" + (i +1);
            channel.basicPublish(EXCHANGE_NAME,routeKey,null,msg.getBytes());
            System.out.println("=====send success==== :" + routeKey + "   " + msg);
        }
        channel.close();
        connection.close();
    }
}
