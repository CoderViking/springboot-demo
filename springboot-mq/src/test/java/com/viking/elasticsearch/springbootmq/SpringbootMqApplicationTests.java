package com.viking.elasticsearch.springbootmq;

import com.viking.elasticsearch.springbootmq.hello.Hello;
import com.viking.elasticsearch.springbootmq.sender.HelloSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootMqApplicationTests {
    @Autowired
    private HelloSender helloSender;

    /////////////////////////////Direct Exchange/////////////////////////////////////
    @Test
    public void hello() {
        helloSender.send();
    }
    @Test
    public void oneToMany() {
        for (int i=0;i<100;i++){
            helloSender.send(i);
        }
    }
    @Test
    public void sendObject() {
        //对象必须实现序列化接口才行
        Hello hello = new Hello();
        hello.say = "This is from Object Hello";
        helloSender.send(hello);
    }
    /////////////////////////////Topic Exchange/////////////////////////////////////
    @Test
    public void topicExchange(){
        helloSender.send1();

        helloSender.send2();
    }
    /////////////////////////////Fanout Exchange/////////////////////////////////////
    @Test
    public void fanoutExchange(){
        helloSender.sendFanout();
    }
    @Test
    public void contextLoads() {

    }

}
