package com.viking.mybatisplus.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Created by Viking on 2019/5/7
 * Druid配置类 注入DruidDataSource
 */
@Primary
@Configuration
@ServletComponentScan// 扫描druid的Servlet接口
public class DruidConfiguration {

    public static final String URL_PATTERNS = "/druid/*";
    public static final String ALLOW_HOST = "127.0.0.1";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "1234";
    public static final String RESET_ENABLE = "true";

    @Bean(name = "druidDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource druidConfiguration(){
        return new DruidDataSource();
    }
}
