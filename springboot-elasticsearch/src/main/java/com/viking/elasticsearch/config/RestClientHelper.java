package com.viking.elasticsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * RestClient 帮助类
 * 使用RestHighLevelClient连接ES服务器
 * Created By Viking on 2020/3/11
 */
@Component
public class RestClientHelper {
    private static String[] ES_HOST;
    @Value("${elasticsearch.host}")
    private void setEsHost(String[] host){
        ES_HOST = host;
    }

    private RestClientHelper(){}
    //双重非空检查锁的单例模式
//    private static volatile RestClientHelper INSTANCE;
//    public static RestClientHelper getInstance(){
//        if (INSTANCE==null){
//            synchronized (RestClientHelper.class){
//                if (INSTANCE==null)
//                    INSTANCE = new RestClientHelper();
//            }
//        }
//        return INSTANCE;
//    }

    //静态内部类实现的单例模式
//    private static class RestClientHelperInstance {
//        // 外部类的单例对象
//        private static final RestClientHelper INSTANCE = new RestClientHelper();
//        // ES客户端的单例对象
//        private static final RestHighLevelClient CLIENT = new RestHighLevelClient(RestClient.builder(parseHost(ES_HOST)));
//    }
//    public static RestClientHelper getInstance(){
//        return RestClientHelperInstance.INSTANCE;
//    }
//    public RestHighLevelClient getClient(){
//        return RestClientHelperInstance.CLIENT;
//    }
    //枚举实现的单例模式
    private enum SingleTonEnum{
        INSTANCE;
        private RestHighLevelClient client;
        SingleTonEnum(){
            client = new RestHighLevelClient(RestClient.builder(parseHost(ES_HOST)));
        }
        private RestHighLevelClient getInstance(){
            return client;
        }
    }
    public static RestHighLevelClient getClient(){
        return SingleTonEnum.INSTANCE.getInstance();
    }


    private static HttpHost[] parseHost(String[] hosts) {
        assert hosts!=null;
        List<HttpHost> list = new ArrayList<>();
        for (String host : hosts){
            if (host.split(":").length == 2){
                String ip = host.split(":")[0];
                int port = Integer.parseInt(host.split(":")[1]);
                list.add(new HttpHost(ip,port,"http"));
            }
        }
        HttpHost[] result = new HttpHost[list.size()];
        assert result.length != 0;
        System.out.println("=============ES客户端初始化完成~"+ list);
        return list.toArray(result);
    }
}