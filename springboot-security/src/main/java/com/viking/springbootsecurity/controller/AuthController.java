package com.viking.springbootsecurity.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created By Viking on 2022/10/24
 * 用户认证接口
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    @RequestMapping("signIn")
    public Object signIn(String username, String password){
        System.out.println("username: " + username + "\tpassword: " + password);
        return "signIn Success!";
    }
}
