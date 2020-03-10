package com.viking.elasticsearch;

import com.viking.elasticsearch.config.ClientHelper;
import com.viking.elasticsearch.elasticsearch.EsIndexManageUtil;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
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
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

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
    public void createIndexTest(){
        Map<String,String> field1 = new HashMap<>();field1.put("name","ap");field1.put("type","string");
        Map<String,String> field2 = new HashMap<>();field2.put("name","apad");field2.put("type","string");
        Map<String,String> field3 = new HashMap<>();field3.put("name","apvu");field3.put("type","float");
        Map<String,String> field4 = new HashMap<>();field4.put("name","apid");field4.put("type","long");
        Map<String,String> field5 = new HashMap<>();field5.put("name","isPerson");field5.put("type","boolean");
        Map<String,String> field6 = new HashMap<>();field6.put("name","date");field6.put("type","date");
        List<Map<String,String>> columnList = Arrays.asList(field1,field2,field3,field4,field5,field6);
        boolean success = EsIndexManageUtil.createNewIndex(null, "transportclient", "type", columnList);
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
        System.out.println(ClientHelper.getClient());
        XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject().startObject("type").startObject("properties");

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
//                contentBuilder.field("index", "not_analyzed").endObject();
            }
        }
        contentBuilder.endObject().endObject().endObject();

        CreateIndexRequest request = new CreateIndexRequest("myindex_02");
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 0)
        );
        request.mapping("type",contentBuilder);
        CreateIndexResponse response = ClientHelper.getClient().indices().create(request);
        boolean success = response.isAcknowledged();
        System.out.println("是否创建成功?"+success);

    }
    @Test// 测试es的使用，插入数据为例
    public void testClient(){
        RestHighLevelClient client = ClientHelper.getClient();
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
        RestHighLevelClient client = ClientHelper.getClient();
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
        RestHighLevelClient client = ClientHelper.getClient();
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
