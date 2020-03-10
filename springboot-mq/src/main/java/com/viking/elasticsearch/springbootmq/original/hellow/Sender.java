package com.viking.elasticsearch.springbootmq.original.hellow;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Viking on 2019/10/7
 * 使用最原始的方式连接rabbitMQ
 * 发送者(生产者)
 */
public class Sender {
    private static final String QUEUE_NAME="helloTest";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()){
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            String message = "Hello Word";
            channel.basicPublish(" ",QUEUE_NAME,null,message.getBytes());
            System.out.println("[X]发送'"+message+"'");
        }
    }
}
