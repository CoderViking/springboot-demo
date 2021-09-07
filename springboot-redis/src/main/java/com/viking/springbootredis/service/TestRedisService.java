package com.viking.springbootredis.service;

import java.util.Map;

/**
 * Created by Viking on 2020/5/2
 */
public interface TestRedisService {
    Object putKey(String key, String value);

    Map<String,Object> getKey(String key);
}
