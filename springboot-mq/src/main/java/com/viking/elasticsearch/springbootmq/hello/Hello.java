package com.viking.elasticsearch.springbootmq.hello;

import java.io.Serializable;

/**
 * Created by Viking on 2019/9/16
 */
public class Hello implements Serializable {
    public String say;

    @Override
    public String toString() {
        return "Hello{" +
                "say='" + say + '\'' +
                '}';
    }
}
