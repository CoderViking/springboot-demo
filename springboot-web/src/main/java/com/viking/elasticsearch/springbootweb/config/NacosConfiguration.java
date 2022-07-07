package com.viking.elasticsearch.springbootweb.config;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
//import com.alibaba.nacos.spring.context.annotation.discovery.EnableNacosDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Viking on 2022/7/7
 */
@Configuration
//@EnableFeignClients
//@EnableNacosDiscovery
@EnableDiscoveryClient
public class NacosConfiguration {
//    @Value("${spring.application.name}")
//    private String applicationName;
//
//    @Value("${server.port}")
//    private Integer port;
//    @NacosInjected
//    private NamingService namingService;
//
//    @PostConstruct
//    private void register(){
//        System.out.println("服务器状态: " + namingService.getServerStatus());
//        try {
//            InetAddress address = InetAddress.getLocalHost();
////            namingService.registerInstance(serviceName, groupName, ip, port, clusterName)
//            // 注册实例到nacos
//            namingService.registerInstance(applicationName,address.getHostAddress(),port);
//        } catch (UnknownHostException | NacosException e) {
//            e.printStackTrace();
//        }
//    }
}
