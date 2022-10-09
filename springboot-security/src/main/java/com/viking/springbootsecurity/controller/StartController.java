package com.viking.springbootsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created By Viking on 2022/10/9
 * 项目启动测试接口
 */
@RestController
@RequestMapping("start")
public class StartController {

    @GetMapping("hello")
    public Object hello(String name){
        System.out.println("收到请求，来自：" + name);
        return "回复: "+ name +" 已收到请求，项目启动正常!";
    }
}
