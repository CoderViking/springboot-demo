package com.viking.springbootthymeleaf.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created By Viking on 2021/9/7
 */
@RestController
@RequestMapping("api")
public class IndexController {
    @PostMapping("table")
    public Object uploadInfo(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> param = new HashMap<>();
        for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
            param.put(stringEntry.getKey(),String.join(";",stringEntry.getValue()));
        }
        System.out.println(param);
        Map<String, Object> result = new HashMap<>();
        result.put("status", "200");
        result.put("msg", "success");
        result.put("data", new HashMap<>());
        return result;
    }
}
