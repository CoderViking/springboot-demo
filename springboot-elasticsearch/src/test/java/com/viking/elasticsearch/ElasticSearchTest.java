package com.viking.elasticsearch;

import com.viking.elasticsearch.config.RestClientHelper;
import com.viking.elasticsearch.elasticsearch.restclient.ESRestClientIndexUtil;
import com.viking.elasticsearch.elasticsearch.transportclient.ESTransportClientIndexUtil;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created By Viking on 2020/3/9
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticSearchTest  {
    private static List<String> fieldTypes = Arrays.asList("boolean","byte","short","int","integer","float","double","long","string","date");

    /*
    String类型，又分两种：
        text：可分词，不可参与聚合
        keyword：不可分词，数据会作为完整字段进行匹配，可以参与聚合
    Numerical：数值类型，分两类
        基本数据类型：long、interger、short、byte、double、float、half_float
        浮点数的高精度类型：scaled_float
        需要指定一个精度因子，比如10或100。elasticsearch会把真实值乘以这个因子后存储，取出时再还原。
    Date：日期类型
        elasticsearch可以对日期格式化为字符串存储，但是建议我们存储为毫秒值，存储为long，节省空间。
    * */

    @Test
    public void deleteIndex(){
        ESRestClientIndexUtil.deleteIndex("book");
//        ESTransportClientIndexUtil.deleteIndex("transportclient_20031017");
    }
    @Test
    public void createIndex(){
        Map<String,String> field1 = new HashMap<>();field1.put("name","ap");field1.put("type","string");
        Map<String,String> field2 = new HashMap<>();field2.put("name","apad");field2.put("type","string");
        Map<String,String> field3 = new HashMap<>();field3.put("name","apvu");field3.put("type","float");
        Map<String,String> field4 = new HashMap<>();field4.put("name","apid");field4.put("type","long");
        Map<String,String> field5 = new HashMap<>();field5.put("name","isPerson");field5.put("type","boolean");
        Map<String,String> field6 = new HashMap<>();field6.put("name","date");field6.put("type","date");
        Map<String,String> field7 = new HashMap<>();field7.put("name","descript");field7.put("type","text");
        List<Map<String,String>> columnList = Arrays.asList(field1,field2,field3,field4,field5,field6,field7);
        ESRestClientIndexUtil.createIndex("rest_03","type",columnList);
    }
    @Test
    public void insertDoc(){
        String indexName = "rest_02";
        String type = "type";
        String esId = UUID.randomUUID().toString();
        Map<String,Object> bean = new HashMap<>();
        bean.put("ap","芒砀山号");
        bean.put("apad","太阳系");
        bean.put("apvu",90.0F);
        bean.put("apid",999999689L);
        bean.put("isPerson",false);
        bean.put("date",new Date().getTime());//不能直接使用Date类型
        bean.put("descript","地球防卫长城");
        ESRestClientIndexUtil.insertDoc(indexName,type,esId,bean);

    }
    @Test
    public void getTest(){
        String indexName = "rest_01";
        String type = "type";
        String esId = "522f2bd9-81b7-4e69-aa1b-d3921e6b4298";
        GetResponse response = ESRestClientIndexUtil.get(indexName, type, esId);
        Map<String, Object> map = response.getSourceAsMap();
        System.out.println(map);
    }
    @Test
    public void searchTest(){
        String indexName = "rest_01";
        String type = "type";
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(type);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(QueryBuilders.termQuery("ap","申请人的主要名称"));
//        boolQueryBuilder.must(QueryBuilders.matchQuery("descript","地球"));//match暂时无法查询，原因时es的服务器版本和客户端版本不一致，6.0.0的服务器缺少对auto_generate_synonyms_phrase_query的支持
        boolQueryBuilder.must(QueryBuilders.boolQuery().should(QueryBuilders.wildcardQuery("apad","*黄土高坡*").boost(10)));
        SearchResponse response = ESRestClientIndexUtil.search(indexName, type, 0, 100, null, null, "apvu", SortOrder.DESC, boolQueryBuilder);
        if (response!=null) {
            for (SearchHit hit : response.getHits().getHits()) {
                System.out.println(hit.getSourceAsString());
//                System.out.println(hit.getSourceAsMap());
            }
        }
    }
    @Test
    public void aggregationsTest(){
        String indexName = "rest_02";
        String type = "type";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(QueryBuilders.boolQuery().should(QueryBuilders.wildcardQuery("descript.keyword","*地球*")));
//        Map<Object, Long> count = ESRestClientIndexUtil.count(indexName, type, null, false, "apvu", boolQueryBuilder);
//        Map<String, LinkedHashMap<Object, Long>> count1 = ESRestClientIndexUtil.count(indexName, type, null,  new String[]{"apvu","apad"}, boolQueryBuilder);
//        System.out.println("count:"+count);
//        System.out.println(count1);
        ESRestClientIndexUtil.max(indexName, type, "apvu", boolQueryBuilder);
        ESRestClientIndexUtil.min(indexName, type, "apvu", boolQueryBuilder);
        ESRestClientIndexUtil.avg(indexName, type, "apvu", boolQueryBuilder);
        ESRestClientIndexUtil.sum(indexName, type, "apvu", boolQueryBuilder);
    }

    @Test
    public void createIndexTest(){
        Map<String,String> field1 = new HashMap<>();field1.put("name","ap");field1.put("type","string");
        Map<String,String> field2 = new HashMap<>();field2.put("name","apad");field2.put("type","string");
        Map<String,String> field3 = new HashMap<>();field3.put("name","apvu");field3.put("type","float");
        Map<String,String> field4 = new HashMap<>();field4.put("name","apid");field4.put("type","long");
        Map<String,String> field5 = new HashMap<>();field5.put("name","isPerson");field5.put("type","boolean");
        Map<String,String> field6 = new HashMap<>();field6.put("name","date");field6.put("type","date");
        List<Map<String,String>> columnList = Arrays.asList(field1,field2,field3,field4,field5,field6);
        boolean success = ESTransportClientIndexUtil.createNewIndex(null, "transportclient", "type", columnList);
        System.out.println(success);
    }

    @Test
    public void voidTest() throws IOException {
        Map<String,String> field1 = new HashMap<>();field1.put("name","ap");field1.put("type","string");
        Map<String,String> field2 = new HashMap<>();field2.put("name","apad");field2.put("type","string");
        Map<String,String> field3 = new HashMap<>();field3.put("name","apvu");field3.put("type","float");
        Map<String,String> field4 = new HashMap<>();field4.put("name","apid");field4.put("type","long");
        Map<String,String> field5 = new HashMap<>();field5.put("name","isPerson");field5.put("type","boolean");
        Map<String,String> field6 = new HashMap<>();field6.put("name","date");field6.put("type","date");
        List<Map<String,String>> columnList = Arrays.asList(field1,field2,field3,field4,field5,field6);


    }
    @Test// 测试es的使用，插入数据为例
    public void testClient(){
        RestHighLevelClient client = RestClientHelper.getClient();
        IndexRequest indexRequest = new IndexRequest("blog", "java","index_08");

        Map<String, Object> blogMap = new HashMap<>();
        blogMap.put("id", "index_08");
        blogMap.put("title", "测试SpringBoot中使用RestHighLevelClient连接es");
        blogMap.put("time", new Date().getTime());
        blogMap.put("mainContent", "主要内容主要内容");
        indexRequest.source(blogMap);
        indexRequest.timeout(TimeValue.timeValueSeconds(1));
        try {
            IndexResponse response = client.index(indexRequest);
            System.out.println(response);
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("done");
    }

    @Test// 测试es的同步方法增删改查
    public void crudTestInEs() throws IOException {
        RestHighLevelClient client = RestClientHelper.getClient();
        Map<String, Object> blogMap = new HashMap<>();
        blogMap.put("id", "index_08");
        blogMap.put("title", "测试SpringBoot中使用RestHighLevelClient连接es");
        blogMap.put("time", new Date().getTime());
        blogMap.put("mainContent", "主要内容主要内容");
        // 增
//        IndexRequest indexRequest = new IndexRequest("指定Index","指定type","指定id");
//        indexRequest.source(blogMap);
//        client.index(indexRequest);
        // 删
//        DeleteRequest deleteRequest = new DeleteRequest("blog", "java","a4J1QGwBjRwxD0F9bvF4");
//        client.delete(deleteRequest);
//        System.out.println("done");
        // 改
//        UpdateRequest updateRequest = new UpdateRequest("指定Index","指定type","指定id");
//        updateRequest.doc(blogMap);
//        client.update(updateRequest);
        // 查
//        GetRequest getRequest = new GetRequest("指定Index","指定type","指定id");
//        getRequest.storedFields(new String[]{});
//        client.get(getRequest);

        SearchRequest searchRequest = new SearchRequest("blog");
        searchRequest.types("java");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();// SearchSourceBuilder使用默认选项创建
//        sourceBuilder.query(QueryBuilders.termQuery("title","FirstPage"));// 设置查询。可以是任何类型的QueryBuilder
//        sourceBuilder.from(0);// 设置from确定结果索引的选项以开始搜索。默认为0。
//        sourceBuilder.size(1);// 设置size确定要返回的搜索匹配数的选项。默认为10。
//        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));// 设置一个可选的超时，控制允许搜索的时间。
//        searchRequest.source(sourceBuilder);// 将SearchSourceBuilder添加到SearchRequest

        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("id","index_04");// 构建查询
//        matchQueryBuilder.fuzziness(Fuzziness.AUTO);// 在匹配查询上启用模糊匹配
        matchQueryBuilder.prefixLength(3);// 在匹配查询上设置前缀长度选项
        matchQueryBuilder.maxExpansions(10);// 设置最大扩展选项以控制查询的模糊过程
        sourceBuilder.query(matchQueryBuilder);
//        sourceBuilder.query(QueryBuilders.matchQuery("title","测试"));
        SearchResponse searchResponse = client.search(searchRequest);
        for (SearchHit hits: searchResponse.getHits().getHits()){
            System.out.println(hits);
            System.out.println(hits.getSourceAsMap());
        }
        System.out.println(searchResponse);
    }
    @Test
    public void crudAsync(){
        RestHighLevelClient client = RestClientHelper.getClient();
        // 增
        IndexRequest indexRequest = new IndexRequest("指定Index","指定type","指定id");
        indexRequest.source();
        client.indexAsync(indexRequest, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        // 删
        DeleteRequest deleteRequest = new DeleteRequest("指定Index","指定type","指定id");
        client.deleteAsync(deleteRequest, new ActionListener<DeleteResponse>() {
            @Override
            public void onResponse(DeleteResponse deleteResponse) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        // 改
        UpdateRequest updateRequest = new UpdateRequest("指定Index","指定type","指定id");
        client.updateAsync(updateRequest, new ActionListener<UpdateResponse>() {
            @Override
            public void onResponse(UpdateResponse updateResponse) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        // 查
        GetRequest getRequest = new GetRequest("指定Index","指定type","指定id");
        client.getAsync(getRequest, new ActionListener<GetResponse>() {
            @Override
            public void onResponse(GetResponse documentFields) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        SearchRequest searchRequest = new SearchRequest();
        client.searchAsync(searchRequest, new ActionListener<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}
