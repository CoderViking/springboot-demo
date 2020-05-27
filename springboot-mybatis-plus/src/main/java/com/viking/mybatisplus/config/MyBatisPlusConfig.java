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
@MapperScan("com.viking.mybatisplus.dao")
@EnableTransactionManagement
@Configuration // 配置类
public class MyBatisPlusConfig {


    @Bean// 注册乐观锁插件
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    @Bean// mybatis-plus分页插件注册
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 分页限制(默认限制为500条，设置为-1表示无限制)
        paginationInterceptor.setLimit(-1);
        return paginationInterceptor;
    }
}
