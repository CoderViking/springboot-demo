package com.viking.springbootredis.controller;

import com.viking.springbootredis.service.TestRedisService;
import com.viking.springbootredis.utils.RedisUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Viking on 2020/5/2
 */
@RestController
@RequestMapping("test")
public class TestController {

    private final TestRedisService service;

    public TestController(TestRedisService service){
        this.service = service;
    }

    @GetMapping("put/{key}/{value}")
    public Object putKey(@PathVariable String key, @PathVariable String value){
        return service.putKey(key,value);
    }
    @GetMapping("get/{key}")
    public Object getKey(@PathVariable String key){
        return service.getKey(key);
    }
}

