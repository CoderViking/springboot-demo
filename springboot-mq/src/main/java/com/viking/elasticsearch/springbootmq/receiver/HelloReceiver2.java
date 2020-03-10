package com.viking.elasticsearch.springbootmq.receiver;

import com.viking.elasticsearch.springbootmq.hello.Hello;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by Viking on 2019/9/17
 * 消息队列-消费者
 */
@Component
@RabbitListener(queues = "hello")
public class HelloReceiver2 {
    @RabbitHandler
    public void process(String hello) {
        System.out.println("Receiver2  : " + hello);
    }
    @RabbitHandler
    public void process(Hello hello) {
        System.out.println("Object2  : " + hello);
    }

}
