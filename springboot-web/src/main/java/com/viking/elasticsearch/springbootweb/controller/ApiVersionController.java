package com.viking.elasticsearch.springbootweb.controller;

import com.viking.elasticsearch.springbootweb.annotation.ApiVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created By Viking on 2021/5/31
 */
@RestController
@RequestMapping("test/{ApiVersion}")
public class ApiVersionController {

    @ApiVersion
    @GetMapping("/test1")
    public String test1(){
        return "Test 1";
    }
    @GetMapping("/test2")
    public String test2(){
        return "Test 2";
    }
    @ApiVersion(3)
    @GetMapping("/test3")
    public String test3(){
        return "Test 3";
    }
}
