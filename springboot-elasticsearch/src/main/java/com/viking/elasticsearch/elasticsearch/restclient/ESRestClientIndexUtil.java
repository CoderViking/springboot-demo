package com.viking.elasticsearch.elasticsearch.restclient;

import com.viking.elasticsearch.config.RestClientHelper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.ParsedMin;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

/**
 * ES创建索引工具类
 * Created By Viking on 2020/3/11
 */
public class ESRestClientIndexUtil {
    private static List<String> fieldTypes = Arrays.asList("boolean","byte","short","int","integer","float","double","long","string","date");

    public static boolean createIndex(@NonNull String indexName, @NonNull String type, @NonNull List<Map<String,String>> columnList){
        System.out.println(RestClientHelper.getClient());
        try {
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
                }else if ("string".equals(fieldType)) {
                    contentBuilder.field("type", "keyword").endObject();
//                    contentBuilder.field("index", true).endObject();
                }else {
                    contentBuilder.field("type", "text").endObject();
//                    contentBuilder.field("index", true).endObject();
                }
            }
            contentBuilder.endObject().endObject().endObject();

            Map<String,String> map = new HashMap<>();
            map.put("type","text");
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            request.settings(Settings.builder()
                    .put("index.number_of_shards", 1)
                    .put("index.number_of_replicas", 0)
                    .put("max_result_window",10000000)
            );
            request.mapping(type,contentBuilder);
            CreateIndexResponse response = RestClientHelper.getClient().indices().create(request);
            boolean success = response.isAcknowledged();
            System.out.println("是否创建成功?"+success);
            return success;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public static boolean deleteIndex(@NonNull String indexName){
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            DeleteIndexResponse response = RestClientHelper.getClient().indices().delete(request);
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
    public static boolean insertDoc(@NonNull String indexName, @NonNull String type, @NonNull String esId, @NonNull Map<String,Object> map){
        IndexRequest indexRequest = new IndexRequest(indexName,type,esId);
        indexRequest.source(map);
        try {
            IndexResponse response = RestClientHelper.getClient().index(indexRequest);
            System.out.println("状态码:"+response.status().getStatus());
            return Boolean.TRUE;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }
    public static GetResponse get(@NonNull String indexName, @NonNull String type, @NonNull String esId){
        GetRequest getRequest = new GetRequest(indexName,type,esId);
        try {
            GetResponse response = RestClientHelper.getClient().get(getRequest);
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
        assertCondition(indexName,type,boolQuery);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        if (from != null) builder.from(from);
        if (size != null) builder.size(size);
        builder.fetchSource(fields,excludeFields);
        if (!Strings.isNullOrEmpty(sortField) && sortOrder != null) builder.sort(sortField,sortOrder);
        builder.query(boolQuery);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(type);
        searchRequest.source(builder);
        try {
            SearchResponse response = RestClientHelper.getClient().search(searchRequest);
            SearchHit[] res = response.getHits().getHits();
            for (SearchHit hit : res){
                System.out.println("res:"+hit.getSourceAsMap());
            }
            System.out.println("total:"+response.getHits().totalHits);
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
            SearchResponse response = RestClientHelper.getClient().search(searchRequest);
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
            SearchResponse response = RestClientHelper.getClient().search(searchRequest);
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
    public static double max(@NonNull String indexName, @NonNull String type, @NonNull String field, @NonNull BoolQueryBuilder boolQuery){
        assertCondition(indexName,type,boolQuery);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        MaxAggregationBuilder maxAggregation = AggregationBuilders.max(field).field(field);
        builder.aggregation(maxAggregation).query(boolQuery);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(type);
        searchRequest.source(builder);
        try {
            SearchResponse response = RestClientHelper.getClient().search(searchRequest);
            Aggregations aggregationsRes = response.getAggregations();
            double max = ((ParsedMax) aggregationsRes.get(field)).getValue();
            System.out.println("max:"+max);
            return max;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0D;
    }
    public static double min(@NonNull String indexName, @NonNull String type, @NonNull String field, @NonNull BoolQueryBuilder boolQuery){
        assertCondition(indexName,type,boolQuery);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        MinAggregationBuilder minAggregation = AggregationBuilders.min(field).field(field);
        builder.aggregation(minAggregation).query(boolQuery);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(type);
        searchRequest.source(builder);
        try {
            SearchResponse response = RestClientHelper.getClient().search(searchRequest);
            Aggregations aggregationsRes = response.getAggregations();
            double min = ((ParsedMin) aggregationsRes.get(field)).getValue();
            System.out.println("min:"+min);
            return min;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0D;
    }
    public static double avg(@NonNull String indexName, @NonNull String type, @NonNull String field, @NonNull BoolQueryBuilder boolQuery){
        assertCondition(indexName,type,boolQuery);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        AvgAggregationBuilder avgAggregation = AggregationBuilders.avg(field).field(field);
        builder.aggregation(avgAggregation).query(boolQuery);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(type);
        searchRequest.source(builder);
        try {
            SearchResponse response = RestClientHelper.getClient().search(searchRequest);
            Aggregations aggregationsRes = response.getAggregations();
            double avg = ((ParsedAvg) aggregationsRes.get(field)).getValue();
            System.out.println("avg:"+avg);
            return avg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0D;
    }
    public static double sum(@NonNull String indexName, @NonNull String type, @NonNull String field, @NonNull BoolQueryBuilder boolQuery){
        assertCondition(indexName,type,boolQuery);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        SumAggregationBuilder sumAggregation = AggregationBuilders.sum(field).field(field);
        builder.aggregation(sumAggregation).query(boolQuery);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(type);
        searchRequest.source(builder);
        try {
            SearchResponse response = RestClientHelper.getClient().search(searchRequest);
            Aggregations aggregationsRes = response.getAggregations();
            double sum = ((ParsedSum) aggregationsRes.get(field)).getValue();
            System.out.println("sum:"+sum);
            return sum;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0D;
    }
    private static void assertCondition(String indexName, String type, BoolQueryBuilder boolQuery){
        Assert.notNull(indexName,"ES索引名称不能为空");
        Assert.notNull(type,"ES索引类型不能为空");
        Assert.notNull(boolQuery,"查询条件不能为空");
    }
}
