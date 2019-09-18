package com.viking.springbootmq.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by Viking on 2019/9/18
 * 消息队列-消费者
 */
@Component
@RabbitListener(queues = "topic.messages")
public class TopicReceiver2 {
    @RabbitHandler
    public void process(String hello) {
        System.out.println("topicReceiver2  : " + hello);
    }
}
