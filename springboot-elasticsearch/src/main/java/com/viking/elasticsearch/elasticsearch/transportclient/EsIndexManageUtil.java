package com.viking.elasticsearch.elasticsearch.transportclient;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import java.util.*;

/**
 * Created By Viking on 2020/3/9
 */
//@Component
public class EsIndexManageUtil {
    private static final String[] ES_HOST = {};
    private EsIndexManageUtil(){}
    private static enum SingleTonEnum{
        INSTANCE;
        private RestHighLevelClient client;
        SingleTonEnum(){
            client = new RestHighLevelClient(RestClient.builder(parseHost(ES_HOST)));
        }
        private RestHighLevelClient getInstance(){
            return client;
        }
    }
    public static RestHighLevelClient getInstance(){
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
