package com.viking.springbootweb.controller;

import com.viking.springbootmq.hello.Hello;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Viking on 2019/9/16
 */
@RestController
public class HelloController {

    @RequestMapping("/")
    public String sayHello(){
        Hello hello = new Hello();
        hello.say = "Hello springboot-MQ~";
        return hello.toString();
    }
}
