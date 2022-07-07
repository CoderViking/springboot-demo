package com.viking.elasticsearch.springbootweb.controller;

import com.viking.elasticsearch.springbootweb.model.OpenFeignResponseModel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Viking on 2022/7/7
 * openfeign测试
 */
@RestController
public class OpenFeignController {

    @RequestMapping("hello")
    public Map<String, Object> openFeign(String name) {
        System.out.println("provider: " + name);
        OpenFeignResponseModel model = new OpenFeignResponseModel();
        model.setStatus(200);
        model.setMsg("success");
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Hello "+ name+  ", from provider ");
        model.setData(data);
        Map<String, Object> result = new HashMap<>();
        result.put("result", model);
        return result;
    }
    @RequestMapping(value = "map", method = {RequestMethod.POST,RequestMethod.GET})
    public OpenFeignResponseModel testResponse(@RequestBody OpenFeignResponseModel param){
        System.out.println("provider map: " + param);
        OpenFeignResponseModel model = new OpenFeignResponseModel();
        model.setMsg("success");
        model.setStatus(200);
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Hello "+ param +  ", from provider ");
        model.setData(data);
        return model;
    }
}
