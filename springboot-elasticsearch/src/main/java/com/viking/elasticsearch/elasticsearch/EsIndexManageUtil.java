package com.viking.elasticsearch.elasticsearch;

import com.viking.elasticsearch.config.ClientHelper;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@Component
public class EsIndexManageUtil {
    public static final int MIN_SHARDS = 5;// 默认的及最小分片数
    public static final int MID_SHARDS = 10;// 中型数量级别分片数
    public static final int MAX_SHARDS = 20;// 大型数量级别分片数
    public static final boolean REPLICAS = true;// 备份
    private static SimpleDateFormat format = new SimpleDateFormat("yyMMddHH");
    private static Logger log = LoggerFactory.getLogger(EsIndexManageUtil.class);
    private static List<String> fieldTypes = Arrays.asList("boolean","byte","short","int","integer","float","double","long","string","date");

    private static Map<String,Integer> HOSTMAP;
//    @Value("elasticsearch.hostMap")
    private Map<String,Integer> hostMap;


    @PostConstruct
    public void init(){
        EsIndexManageUtil.HOSTMAP = new HashMap<>();
        HOSTMAP.put("172.16.2.81",9300);
    }

    public static Client getClient(){
        //        创建settings对象
        Settings settings = Settings.builder()
                .put("cluster.name","my-application")
                .build();
//        创建客户端对象
        TransportClient client = new PreBuiltTransportClient(settings);
        try {
            TransportAddress[] transportAddress = new TransportAddress[HOSTMAP.size()];
            List<TransportAddress> addressList = new ArrayList<>();
            int index = 0;
            for (String ip : HOSTMAP.keySet()) {
                transportAddress[index++] = new TransportAddress(InetAddress.getByName(ip), HOSTMAP.get(ip));
            }
            client.addTransportAddresses(transportAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
//        关闭资源
//        client.close();
        return client;
    }


    /**
     * 创建新的es索引
     * @param tableName 数据表名称
     * @param indexName ES索引名称
     * @param type ES索引类型
     * @param shards 索引的分片数
     * @param replicas 是否备份
     * @param columnList 字段列表
     * @return 创建是否成功
     */
    public static boolean createNewIndex(String tableName,String indexName, String type, int shards, boolean replicas,
                                         List<Map<String,String>> columnList){
        try {
            Settings settings = Settings.builder()
                    .put("number_of_shards",shards)
                    .put("number_of_replicas",replicas?1:0)
                    .put("max_result_window",10000000)
                    .build();
            indexName = indexName.toLowerCase() + "_" + format.format(new Date());

            getClient().admin().indices().prepareCreate(indexName).setSettings(settings).execute().actionGet();
            XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject().startObject(type).startObject("properties");

            for (Map<String,String> column : columnList) {
                if (!fieldTypes.contains(column.get("type").toLowerCase())) continue;
                contentBuilder.startObject(column.get("name"));
                String fieldType = column.get("type").toLowerCase();

                if (Arrays.asList("byte","short","int","integer","long","date").contains(fieldType)){
                    contentBuilder.field("type", "long").endObject();
                }else if (Arrays.asList("float","double").contains(fieldType)){
                    contentBuilder.field("type", "double").endObject();
                }else if ("boolean".equals(fieldType)){
                    contentBuilder.field("type", "boolean").endObject();
                }else {
                    contentBuilder.field("type", "keyword").endObject();
//                    contentBuilder.field("index", "not_analyzed")
                }
            }
            contentBuilder.endObject().endObject().endObject();

            PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(type).source(contentBuilder);
            PutMappingResponse response = getClient().admin().indices().putMapping(mapping).actionGet();
            boolean success = response.isAcknowledged();
//            if (success) {
//                int result = DBUtils.insertEsCreatedIndex(tableName,indexName, type);
//                log.info("table:" + tableName + "  es索引创建成功,索引名称:" + indexName);
//                return result > 0;
//            }
            return success;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }
    public static boolean createNewIndex(String tableName,String indexName, String type, List<Map<String,String>> columnList){
        return createNewIndex(tableName,indexName,type,MIN_SHARDS,REPLICAS,columnList);
    }
    public static boolean createNewIndex(String tableName,String indexName, String type, int shards, boolean replicas, Class clazz, List<String> excludeFieldList){
        Set<Field> fieldSet = new LinkedHashSet<>();
        fieldSet.addAll(Arrays.asList(clazz.getFields()));
        fieldSet.addAll(Arrays.asList(clazz.getDeclaredFields()));
        List<Map<String,String>> columnList = new ArrayList<>();
        for (Field field : fieldSet){
            String fieldName = field.getName();
            if (!CollectionUtils.isEmpty(excludeFieldList)
                    && excludeFieldList.contains(fieldName)) continue;
            String typeName = field.getType().getSimpleName();
            Map<String,String> column = new HashMap<>();
            column.put("name",fieldName);column.put("type",typeName.toLowerCase());
            columnList.add(column);
        }
        return createNewIndex(tableName,indexName,type,shards,replicas,columnList);
    }
    public static boolean createNewIndex(String tableName,String indexName, String type, Class clazz){
        return createNewIndex(tableName,indexName,type,MIN_SHARDS,REPLICAS,clazz,null);
    }
}
