package com.viking.springbootredis.service.impl;

import com.viking.springbootredis.service.TestRedisService;
import com.viking.springbootredis.utils.RedisUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Viking on 2020/5/2
 */
@Service
public class TestRedisServiceImpl implements TestRedisService {
    private final RedisUtils<Object> redisUtils;


    public TestRedisServiceImpl(RedisUtils<Object> redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public Object putKey(String key, String value) {
        redisUtils.set(key,value);
        return "OK";
    }

    @Override
    public Map<String, Object> getKey(String key) {
        Object value = redisUtils.get(key);
        Map<String,Object> result = new HashMap<>();
        result.put("key",key);
        result.put("value",value);
        return result;
    }
}
