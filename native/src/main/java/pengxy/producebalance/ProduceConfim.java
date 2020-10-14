package pengxy.producebalance;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProduceConfim {

    public final static String EXCHANGE_NAME = "producer_confirm";
    private final static String ROUTE_KEY = "武田";

    public static void main(String[] args) throws IOException, TimeoutException,InterruptedException{
        ConnectionFactory factory = new ConnectionFactory();
        // 设置MabbitMQ所在主机ip或者主机名
        factory.setHost("127.0.0.1");
        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个信道
        Channel channel = connection.createChannel();
        // 指定转发
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 启用发送者确认模式
        channel.confirmSelect();
        for(int i=0;i<2;i++){
            // 发送的消息
            String message = "Hello World_"+(i+1);
            //参数1：exchange name
            //参数2：routing key
            channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY, true,null, message.getBytes());
            System.out.println(" Sent Message: [" + ROUTE_KEY +"]:'"+ message + "'");
            //TODO
            //确认是否成功(true成功)
            if(channel.waitForConfirms()){
                System.out.println("send success");
            }else{
                System.out.println("send failure");
            }
        }
    }
}
