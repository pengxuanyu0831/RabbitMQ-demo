package pengxy.producebalance.transation;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TransationProduce {
    public static final String EXCHANGE_NAME = "transation_log";

    public static void main(String[] args) throws TimeoutException, IOException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("localhost");

        Connection connection = connectionFactory.newConnection();
        // 创建一个信道
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String[] routeKeys = {"筱田", "桃乃", "武田"};
        // 启用事物
        channel.txSelect();
        try {
            for(int i =0;i<3;i++){
                String routekey = routeKeys[i%3];
                // 发送的消息
                String message = "Hello World_"+(i+1)
                        +("_"+System.currentTimeMillis());
                channel.basicPublish(EXCHANGE_NAME, routekey, true,
                        null, message.getBytes());
                System.out.println("----------------------------------");
                System.out.println(" Sent Message: [" + routekey +"]:'"
                        + message + "'");
                Thread.sleep(200);
            }
            channel.txCommit();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        channel.close();
        connection.close();

    }
}
