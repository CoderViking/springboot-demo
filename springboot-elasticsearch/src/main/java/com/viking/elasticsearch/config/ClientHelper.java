//package com.viking.elasticsearch.config;
//
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Created By Viking on 2020/3/9
// */
//@Component
//public class ClientHelper {
//    private static String[] ES_HOST;
//
//    @Value("${elasticsearch.host}")
//    public void setEsHost(String[] host){
//        ES_HOST = host;
//    }
//
//    public static RestHighLevelClient getClient(){
//        // RestHighLevelClient构建方法
//        //  唯一一个开放的有参构造器是传入一个RestClient【RestLowLevelClient】对象
//        return new RestHighLevelClient(RestClient.builder(parseHost(ES_HOST)));
//    }
//    private static HttpHost[] parseHost(String[] hosts) {
//        assert hosts!=null;
//        List<HttpHost> list = new ArrayList<>();
//        for (String host : hosts){
//            if (host.split(":").length == 2){
//                String ip = host.split(":")[0];
//                int port = Integer.parseInt(host.split(":")[1]);
//                list.add(new HttpHost(ip,port,"http"));
//            }
//        }
//        HttpHost[] result = new HttpHost[list.size()];
//        assert result.length != 0;
//        System.out.println("ES客户端初始化完成~"+ list);
//        return list.toArray(result);
//    }
//}
