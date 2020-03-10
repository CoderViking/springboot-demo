package com.viking.elasticsearch.springbootmq.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by Viking on 2019/9/18
 * 消息队列-消费者
 */
@Component
@RabbitListener(queues = "fanout.A")
public class FanoutReceiverA {
    @RabbitHandler
    public void process(String hello) {
        System.out.println("fanout Receiver A: " + hello);
    }
}
