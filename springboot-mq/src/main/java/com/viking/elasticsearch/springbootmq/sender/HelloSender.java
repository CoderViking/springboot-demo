package com.viking.elasticsearch.springbootmq.sender;

import com.viking.elasticsearch.springbootmq.hello.Hello;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Viking on 2019/9/17
 * 消息队列-生产者
 */
@Component
public class HelloSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    /////////////////////////////Direct Exchange/////////////////////////////////////
    public void send() {
        String context = "hello " + new Date();
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }
    public void send(int index) {
        String context = "hello " + new Date()+"--"+index;
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }
    public void send(Hello hello) {
        System.out.println("Sender : " + hello);
        this.rabbitTemplate.convertAndSend("hello", hello);
    }
    /////////////////////////////Topic Exchange/////////////////////////////////////
    public void send1() {
        String context = "hi, i am message 1";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("exchange", "topic.message", context);
    }
    public void send2() {
        String context = "hi, i am messages 2";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("exchange", "topic.messages", context);
    }
    /////////////////////////////Fanout Exchange/////////////////////////////////////
    public void sendFanout() {
        String context = "hi, fanout msg ";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("fanoutExchange","", context);
    }

}
