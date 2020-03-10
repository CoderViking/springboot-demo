package com.viking.elasticsearch.springbootmq.original.hellow;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Viking on 2019/10/7
 *  * 使用最原始的方式连接rabbitMQ
 *  * 接收者(消费者)
 */
public class Recv {
    private static final String QUEUE_NAME = "helloTest";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPort(5672);
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        System.out.println("[*]等待消息...要退出, 请按CTRL + C");
        DeliverCallback deliveryCallback =(consumerTag, delivery)-> {
            String message = new  String(delivery.getBody(),"UTF-8");
            System.out.println(" [x]收到'" +message+ "'");
        };
        channel.basicConsume(QUEUE_NAME,true,deliveryCallback,ConsumerTag-> {});
    }
}
