package com.viking.elasticsearch.elasticsearch.transportclient;

import com.viking.elasticsearch.config.TransportClientHelper;
import com.viking.elasticsearch.elasticsearch.restclient.ESRestClientIndexUtil;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
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
