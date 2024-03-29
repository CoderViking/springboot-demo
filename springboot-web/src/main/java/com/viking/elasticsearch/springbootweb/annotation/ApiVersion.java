package com.viking.elasticsearch.springbootweb.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * Created By Viking on 2021/5/31
 */

@Target({ElementType.METHOD, ElementType.TYPE})//用于方法和类上
@Retention(RetentionPolicy.RUNTIME)//运行时有效
@Documented //标识这是个注解并应该被 javadoc工具记录
@Mapping //标识映射
public @interface ApiVersion {
    /**
     * 接口版本号取值
     * @return 版本号
     */
    int value() default 1;
}
