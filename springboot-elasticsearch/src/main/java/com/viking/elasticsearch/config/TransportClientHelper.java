package com.viking.elasticsearch.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * TransportClient 帮助类
 * 使用TransportClient连接ES服务器
 * Created By Viking on 2020/3/11
 */
@Component
public class TransportClientHelper {
    private static String[] ES_HOST;
    private static String CLUSTER_NAME;
    @Value("${elasticsearch.es_host}")
    private void setEsHost(String[] esHost){
        TransportClientHelper.ES_HOST = esHost;
    }
    @Value("${elasticsearch.cluster_name}")
    private void setClusterName(String clusterName){
        TransportClientHelper.CLUSTER_NAME = clusterName;
    }

    private TransportClientHelper(){}
    //静态内部类实现的单例模式
    private static class TransportClientHelperInstance {
        // 外部类的单例对象
        private static final TransportClientHelper INSTANCE = new TransportClientHelper();
        // ES客户端的单例对象
        private static final Client CLIENT = TransportClientHelper.initClient();
    }

    public static TransportClientHelper getInstance(){
        return TransportClientHelperInstance.INSTANCE;
    }
    public Client getClient(){
        return TransportClientHelperInstance.CLIENT;
    }
    public static Client initClient(){
        // 创建settings对象
        Settings settings = Settings.builder()
                .put("cluster.name",CLUSTER_NAME)
                .build();
        // 创建客户端对象
        TransportClient client = new PreBuiltTransportClient(settings);
        try {
            TransportAddress[] transportAddress = new TransportAddress[ES_HOST.length];
            int index = 0;
            for (String host : ES_HOST) {
                String[] hosts = host.split(":");
                if (hosts.length!=2) continue;
                transportAddress[index++] = new TransportAddress(InetAddress.getByName(hosts[0]), Integer.parseInt(hosts[1]));
            }
            client.addTransportAddresses(transportAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        // 关闭资源
//        client.close();
        return client;
    }
}
