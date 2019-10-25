package com.viking.springbootweb.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Viking on 2019/10/23
 * 策略模式的经典案例
 */
public class StrategyDemo {
    public static void main(String[] args) {
        System.out.println(execute.get(1).save("传入参数 data"));
    }
    public interface Execute{
        String save(String data);
    }

    public static Map<Integer,Execute> execute = new HashMap<>();

    static {
        execute.put(1,(data) -> {
            System.out.println("策略1执行");
           return data;
        });

        execute.put(2,(data) -> {
            System.out.println("策略2执行");
           return data;
        });
    }
}
