package com.viking.elasticsearch.elasticsearch.restclient;

import com.viking.elasticsearch.config.RestClientHelper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.reindex.AbstractBulkByScrollRequest;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.index.reindex.RemoteInfo;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * ES创建索引工具类
 * Created By Viking on 2020/3/11
 */
public class ESRestClientIndexUtil {
    private static List<String> fieldTypes = Arrays.asList("boolean","byte","short","int","integer","float","double","long","string","date","keyword");

    /**
     * 创建ES索引
     * @param indexName 索引名称
     * @param alias 索引别称
     * @param columnList 列名称和类型
     * @return 创建是否成功
     */
    public static boolean createIndex(@NonNull String indexName, @Nullable String alias, @NonNull List<Map<String,String>> columnList){
        try {
            // 先判断该索引名称是否已经存在
            GetIndexRequest existsRequest = new GetIndexRequest(indexName);
            boolean exists = RestClientHelper.getClient().indices().exists(existsRequest, RequestOptions.DEFAULT);
            if (exists){
                System.out.println("索引["+indexName+"]已存在，创建失败~");
                return Boolean.FALSE;
            }
            // 映射索引的mapping
            XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject().startObject("properties");
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
                }else if ("string".equals(fieldType)) {
                    contentBuilder.field("type", "keyword").endObject();
//                    contentBuilder.field("index", true).endObject();
                }else if ("keyword".equals(fieldType)) {
                    contentBuilder.field("type", "keyword").endObject();
//                    contentBuilder.field("index", true).endObject();
                }else {
                    contentBuilder.field("type", "text").endObject();
//                    contentBuilder.field("index", true).endObject();
                }
            }
            contentBuilder.endObject().endObject();
            // 创建索引
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            // 配置索引相关的设置
            request.settings(Settings.builder()/*.putProperties()*/
                    .put("index.number_of_shards", 5)
                    .put("index.number_of_replicas", 0)
                    .put("index.max_result_window",10000000)

//                    .put("index.analysis",new HashMap<>())
/** "index": {
    "number_of_shards": "10",
    "max_result_window": "100000000",
    "creation_date": "1516940588517",
    "analysis": {
      "analyzer": {
        "uppercase": {
          "filter": "uppercase",
          "type": "custom",
          "tokenizer": "standard"
        },
        "my_analyzer": {
          "tokenizer": "my_tokenizer"
        }
      },
      "tokenizer": {
        "my_tokenizer": {
          "pattern": ";",
          "type": "pattern"
        }
      }
    }
  }*/
            );
            request.mapping(contentBuilder);
            if (!Strings.isNullOrEmpty(alias)) request.alias(new Alias(alias));
            CreateIndexResponse response = RestClientHelper.getClient().indices().create(request, RequestOptions.DEFAULT);
            boolean success = response.isAcknowledged();
            System.out.println("是否创建成功?"+success);
            return success;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    /**
     * 删除ES索引
     * @param indexName 索引名称
     * @return 是否成功
     */
    public static boolean deleteIndex(@NonNull String indexName){
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            AcknowledgedResponse response = RestClientHelper.getClient().indices().delete(request, RequestOptions.DEFAULT);
            boolean success = response.isAcknowledged();
            System.out.println("=============索引["+indexName+"]删除成功~");
            return success;
        }catch (ElasticsearchException exception) {
            if (exception.status() == RestStatus.NOT_FOUND) {
                System.out.println("=============索引["+indexName+"]不存在~");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    /**
     * 修改索引的设置
     * @param indexName 索引名称
     * @return 是否修改成功
     */
    public static boolean updateIndexSetting(String indexName){
        UpdateSettingsRequest request = new UpdateSettingsRequest(indexName);
        request.settings(Settings.builder().put("index.blocks.write",true));
        try {
            AcknowledgedResponse response = RestClientHelper.getClient().indices().putSettings(request, RequestOptions.DEFAULT);
            System.out.println("修改索引设置是否操作成功？" + response.isAcknowledged());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }

    /**
     * 缩小索引，将原先的索引缩小为另一个新的索引，
     * 新索引的分片数必须是原索引分片数的因数，
     * 如果原索引的分片数为质数，则新索引的分片数只能是1和原索引分片数本身
     * @param targetIndex 目标索引
     * @param sourceIndex 原始索引
     * @return 是否操作成功
     */
    public static boolean shrinkIndex(String targetIndex, String sourceIndex){
        updateIndexSetting(sourceIndex);
        ResizeRequest request = new ResizeRequest(targetIndex, sourceIndex);
        request.setSettings(Settings.builder()
                .put("index.number_of_shards",5)
                .put("index.number_of_replicas", 0)
                .build());
        try {
            ResizeResponse response = RestClientHelper.getClient().indices().shrink(request, RequestOptions.DEFAULT);
            boolean success = response.isAcknowledged();
            System.out.println("缩小索引操作是否成功?" + success);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }

    /**
     * 插入文档
     * @param indexName 索引名称
     * @param esId es的文档存储id
     * @param map 文档的字段和值
     * @return 是否成功
     */
    public static boolean insertDoc(@NonNull String indexName, @NonNull String esId, @NonNull Map<String,Object> map){
        IndexRequest indexRequest = new IndexRequest(indexName).id(esId).source(map);
        indexRequest.source(map);
        try {
            IndexResponse response = RestClientHelper.getClient().index(indexRequest, RequestOptions.DEFAULT);
            System.out.println("状态码:"+response.status().getStatus());
            return Boolean.TRUE;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    /**
     * 删除文档
     * @param indexName 索引名称
     * @param esId ES的文档存储Id
     * @return 是否成功
     */
    public static boolean deleteDoc(String indexName, String esId){
        DeleteRequest request = new DeleteRequest(indexName, esId);
        try {
            DeleteResponse response = RestClientHelper.getClient().delete(request, RequestOptions.DEFAULT);
            if (response.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                System.out.println("未找到该ID的文档");
            }
            System.out.println("状态码:"+response.status().getStatus());
            return Boolean.TRUE;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    /**
     * 更新文档
     * @param indexName 索引名称
     * @param esId ES的文档存储ID
     * @param jsonMap 文档对应的字段和值
     * @return 是否成功
     */
    public static boolean updateDoc(String indexName, String esId, Map<String, Object> jsonMap){
        UpdateRequest request = new UpdateRequest(indexName,esId).upsert(jsonMap);
        try {
            UpdateResponse response = RestClientHelper.getClient().update(request, RequestOptions.DEFAULT);
            System.out.println("状态码:" + response.status().getStatus());
            return Boolean.TRUE;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    /**
     * 通过esId获取文档
     * @param indexName 索引名称
     * @param esId ES的文档存储ID
     * @return 是否成功
     */
    public static Map<String, Object> getDoc(String indexName, String esId){
        GetRequest request = new GetRequest(indexName,esId);
        try {
            GetResponse response = RestClientHelper.getClient().get(request, RequestOptions.DEFAULT);
            if (response.isExists()){
                System.out.println(response.getSourceAsMap());
                return response.getSourceAsMap();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    /**
     * 通过多个esId获取文档
     * @param indexName 索引名称
     * @param esId ES的文档存储ID
     * @return 是否成功
     */
    public static List<Map<String, Object>> getDoc(String indexName, List<String> esId){
        MultiGetRequest request = new MultiGetRequest();
        for (String id : esId) {
            request.add(new MultiGetRequest.Item(indexName, id));
        }
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            MultiGetResponse responses = RestClientHelper.getClient().mget(request, RequestOptions.DEFAULT);
            for (MultiGetItemResponse response : responses.getResponses()) {
                list.add(response.getResponse().getSourceAsMap());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 验证指定esId的文档是否存在
     * @param indexName 索引名称
     * @param esId esId
     * @return 查询结果
     */
    public static boolean existsDoc(String indexName, String esId){
        GetRequest request = new GetRequest(indexName,esId);
        try {
            return RestClientHelper.getClient().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    /**
     * 重新索引，es数据迁移
     * @param sourceIndex 源索引名称集合
     * @param targetIndex 目标索引名称
     * @param host 远程es集群主机地址
     * @param port 远程es集权主机端口
     * @return 操作是否成功
     */
    public static boolean reIndexDoc(String[] sourceIndex, String targetIndex, String host, int port){
        ReindexRequest request = new ReindexRequest();
        request.setSourceIndices(sourceIndex);//设置源索引名称(一个或多个)
        request.setDestIndex(targetIndex);//设置目标索引名称
        request.setDestVersionType(VersionType.EXTERNAL);
        request.setDestOpType("create");
        request.setSourceBatchSize(10000);// 批量处理文档数量
//        request.setSlices(10);
        request.setTimeout(TimeValue.timeValueMinutes(2));
        request.setRefresh(true);// 迁移完成后是否刷新索引
        request.setTimeout(new TimeValue(100, TimeUnit.SECONDS));
        request.setRemoteInfo(
                new RemoteInfo(
                        "http", host, port, null,
                        new BytesArray(new MatchAllQueryBuilder().toString()),
                        null, null, Collections.emptyMap(),
                        new TimeValue(100, TimeUnit.SECONDS),
                        new TimeValue(100, TimeUnit.SECONDS)
                )
        );

        try {
            long start = System.currentTimeMillis();
            BulkByScrollResponse bulkResponse =
                    RestClientHelper.getClient().reindex(request, RequestOptions.DEFAULT);
            long total = bulkResponse.getTotal();
            System.out.println("总计转移数据量:" + total + "\t总共耗时约: "+ (System.currentTimeMillis() - start)/1000 + "秒");
            return Boolean.TRUE;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }
    public static GetResponse get(@NonNull String indexName, @NonNull String esId){
        GetRequest getRequest = new GetRequest(indexName,esId);
        try {
            GetResponse response = RestClientHelper.getClient().get(getRequest, RequestOptions.DEFAULT);
            Map<String, Object> map = response.getSourceAsMap();
            System.out.println(map);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static SearchResponse search(@NonNull String indexName, @NonNull String type, @Nullable Integer from,
                                        @Nullable Integer size, @Nullable String[] fields, @Nullable String[] excludeFields,
                                        @Nullable String sortField, @Nullable SortOrder sortOrder, @NonNull BoolQueryBuilder boolQuery){
        assertCondition(indexName,null,boolQuery);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        if (from != null) builder.from(from);
        if (size != null) builder.size(size);
        builder.fetchSource(fields,excludeFields);
        if (!Strings.isNullOrEmpty(sortField) && sortOrder != null) builder.sort(sortField,sortOrder);
        builder.query(boolQuery);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(builder);
        try {
            SearchResponse response = RestClientHelper.getClient().search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] res = response.getHits().getHits();
            for (SearchHit hit : res){
                System.out.println("res:"+hit.getSourceAsMap());
            }
            System.out.println("total:"+response.getHits().getTotalHits().value);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<Object,Long> count(@NonNull String indexName, @NonNull String type,@Nullable Integer size,
                                         @Nullable Boolean isAsc, @NonNull String field, @NonNull BoolQueryBuilder boolQuery){
        assertCondition(indexName,type,boolQuery);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        TermsAggregationBuilder aggregation = AggregationBuilders.terms(field).field(field);
        if (size != null) aggregation.size(size);
        if (isAsc != null) aggregation.order(BucketOrder.key(isAsc));
        builder.aggregation(aggregation).query(boolQuery);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(type);
        searchRequest.source(builder);
        try {
            SearchResponse response = RestClientHelper.getClient().search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregationsRes = response.getAggregations();
            Terms terms = aggregationsRes.get(field);
            List<? extends Terms.Bucket> list = terms.getBuckets();
            Map<Object,Long> result = new LinkedHashMap<>();
            for (Terms.Bucket bucket : list) {
                result.put(bucket.getKey(),bucket.getDocCount());
                System.out.println(bucket.getKey()+"--"+bucket.getDocCount());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedHashMap<>();
    }
    public static Map<String,LinkedHashMap<Object,Long>> count(@NonNull String indexName, @NonNull String type,@Nullable Integer size,
                                         @NonNull String[] fields, @NonNull BoolQueryBuilder boolQuery){
        assertCondition(indexName,type,boolQuery);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        for (String field : fields) {
            TermsAggregationBuilder aggregation = AggregationBuilders.terms(field).field(field);
            if (size != null) aggregation.size(size);
            builder.aggregation(aggregation);
        }
        builder.query(boolQuery);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(type);
        searchRequest.source(builder);
        try {
            SearchResponse response = RestClientHelper.getClient().search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregationsRes = response.getAggregations();
            Map<String,LinkedHashMap<Object,Long>> result = new HashMap<>();
            for (String field : fields) {
                Terms terms = aggregationsRes.get(field);
                List<? extends Terms.Bucket> list = terms.getBuckets();
                LinkedHashMap<Object,Long> fieldMap = new LinkedHashMap<>();
                for (Terms.Bucket bucket : list) {
                    fieldMap.put(bucket.getKey(),bucket.getDocCount());
                    System.out.println(bucket.getKey()+"--"+bucket.getDocCount());
                }
                result.put(field,fieldMap);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
//    public static double max(@NonNull String indexName, @NonNull String type, @NonNull String field, @NonNull BoolQueryBuilder boolQuery){
//        assertCondition(indexName,type,boolQuery);
//        SearchSourceBuilder builder = new SearchSourceBuilder();
//        MaxAggregationBuilder maxAggregation = AggregationBuilders.max(field).field(field);
//        builder.aggregation(maxAggregation).query(boolQuery);
//        SearchRequest searchRequest = new SearchRequest(indexName);
//        searchRequest.types(type);
//        searchRequest.source(builder);
//        try {
//            SearchResponse response = RestClientHelper.getClient().search(searchRequest);
//            Aggregations aggregationsRes = response.getAggregations();
//            double max = ((ParsedMax) aggregationsRes.get(field)).getValue();
//            System.out.println("max:"+max);
//            return max;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return 0.0D;
//    }
//    public static double min(@NonNull String indexName, @NonNull String type, @NonNull String field, @NonNull BoolQueryBuilder boolQuery){
//        assertCondition(indexName,type,boolQuery);
//        SearchSourceBuilder builder = new SearchSourceBuilder();
//        MinAggregationBuilder minAggregation = AggregationBuilders.min(field).field(field);
//        builder.aggregation(minAggregation).query(boolQuery);
//        SearchRequest searchRequest = new SearchRequest(indexName);
//        searchRequest.types(type);
//        searchRequest.source(builder);
//        try {
//            SearchResponse response = RestClientHelper.getClient().search(searchRequest);
//            Aggregations aggregationsRes = response.getAggregations();
//            double min = ((ParsedMin) aggregationsRes.get(field)).getValue();
//            System.out.println("min:"+min);
//            return min;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return 0.0D;
//    }
//    public static double avg(@NonNull String indexName, @NonNull String type, @NonNull String field, @NonNull BoolQueryBuilder boolQuery){
//        assertCondition(indexName,type,boolQuery);
//        SearchSourceBuilder builder = new SearchSourceBuilder();
//        AvgAggregationBuilder avgAggregation = AggregationBuilders.avg(field).field(field);
//        builder.aggregation(avgAggregation).query(boolQuery);
//        SearchRequest searchRequest = new SearchRequest(indexName);
//        searchRequest.types(type);
//        searchRequest.source(builder);
//        try {
//            SearchResponse response = RestClientHelper.getClient().search(searchRequest);
//            Aggregations aggregationsRes = response.getAggregations();
//            double avg = ((ParsedAvg) aggregationsRes.get(field)).getValue();
//            System.out.println("avg:"+avg);
//            return avg;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return 0.0D;
//    }
//    public static double sum(@NonNull String indexName, @NonNull String type, @NonNull String field, @NonNull BoolQueryBuilder boolQuery){
//        assertCondition(indexName,type,boolQuery);
//        SearchSourceBuilder builder = new SearchSourceBuilder();
//        SumAggregationBuilder sumAggregation = AggregationBuilders.sum(field).field(field);
//        builder.aggregation(sumAggregation).query(boolQuery);
//        SearchRequest searchRequest = new SearchRequest(indexName);
//        searchRequest.types(type);
//        searchRequest.source(builder);
//        try {
//            SearchResponse response = RestClientHelper.getClient().search(searchRequest);
//            Aggregations aggregationsRes = response.getAggregations();
//            double sum = ((ParsedSum) aggregationsRes.get(field)).getValue();
//            System.out.println("sum:"+sum);
//            return sum;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return 0.0D;
//    }
    private static void assertCondition(String indexName, String type, BoolQueryBuilder boolQuery){
        Assert.notNull(indexName,"ES索引名称不能为空");
//        Assert.notNull(type,"ES索引类型不能为空");
        Assert.notNull(boolQuery,"查询条件不能为空");
    }
}
