package com.viking.mybatisplus.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Created By Viking on 2020/3/20
 */
// 扫描我们的 mapper 文件夹
@MapperScan("com.viking.mybatisplus.**.mapper")
@EnableTransactionManagement
@Configuration // 配置类
public class MyBatisPlusConfig {

    // 注册乐观锁插件
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
//
//    // 分页插件
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        return  new PaginationInterceptor();
//    }
}
