package com.viking.springbootthymeleaf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created By Viking on 2021/9/7
 */
@Controller
@RequestMapping("page")
@Slf4j
public class RouteController {

    @GetMapping("/{path}")
    public String route(@PathVariable String path) {
        log.info("=== route page path: " + path);
        return "main/" + path;
    }
}
