package pengxy.producebalance.mandatory;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerMandatory {
    public final static String EXCHANGE_NAME = "mandatory_test";

    public static void main(String[] args)throws IOException, TimeoutException {
        // 创建连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 默认端口5672
        connectionFactory.setHost("localhost");
        // 标准写法的创建连接
        Connection connection = connectionFactory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 失败通知的回调方法
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replayText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String();
                System.out.println("返回的值 :" + replyCode);
                System.out.println("返回的信息 :" + replayText);
            }
        });

        String[] routeKeys = {"筱田","桃乃","武田"};
        for(int i=0;i<3;i++){
            String routeKey = routeKeys[i%3];
            String msg = "Hello RabbitMQ" + (i +1);
            // 第三个参数是返回值，详情点进去看这个方法的源码
            channel.basicPublish(EXCHANGE_NAME,routeKey,true,null,msg.getBytes());
            System.out.println("=====send success==== :" + routeKey + "   " + msg);
        }


    }
}
