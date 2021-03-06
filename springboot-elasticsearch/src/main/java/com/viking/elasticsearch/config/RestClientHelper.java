package com.viking.elasticsearch.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
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
    private static String ES_NAME;
    private static String ES_PASSWORD;
    @Value("${elasticsearch.host}")
    private void setEsHost(String[] host){
        ES_HOST = host;
    }
    @Value("${elasticsearch.name}")
    private void setEsName(String name){
        ES_NAME = name;
    }
    @Value("${elasticsearch.password}")
    private void setEsPassword(String password){
        ES_PASSWORD = password;
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
            client = new RestHighLevelClient(RestClient.builder(parseHost(ES_HOST)).setRequestConfigCallback(builder -> {
                builder.setConnectTimeout(30000)// 更改客户端的连接超时时间默认1秒现在改为30秒
                        .setSocketTimeout(20601000);//更改客户端的socket连接超时限制默认30秒现在改为20分钟
                return builder;
            })/*.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(createCredentials()))*/);// 需要通过账号密码访问时，取消注释即可
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
    // 配置访问ES的账号密码凭证
    private static CredentialsProvider createCredentials(){
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(ES_NAME, ES_PASSWORD));
        return credentialsProvider;
    }

}