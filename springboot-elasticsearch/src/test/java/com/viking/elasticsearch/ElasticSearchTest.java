package com.viking.elasticsearch;

import com.viking.elasticsearch.config.RestClientHelper;
import com.viking.elasticsearch.elasticsearch.restclient.ESRestClientIndexUtil;
import com.viking.elasticsearch.elasticsearch.transportclient.ESTransportClientIndexUtil;
import com.viking.elasticsearch.entity.SuperSeminary;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.Alias;
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
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static List<String> fieldTypes = Arrays.asList("boolean","byte","short","int","integer","float","double","long","string","date","keyword","text");

    private static Logger log = LoggerFactory.getLogger(ElasticSearchTest.class);
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
        ESRestClientIndexUtil.deleteIndex("cn_xz_area");
//        ESTransportClientIndexUtil.deleteIndex("transportclient_20031017");
    }
    @Test
    public void shrinkIndex(){
//        ESRestClientIndexUtil.shrinkIndex("area_copy","cn_xz_area");
    }
    @Test
    public void createIndex(){
        Map<String,String> field1 = new HashMap<>();field1.put("name","name");field1.put("type","keyword");// 名称
        Map<String,String> field2 = new HashMap<>();field2.put("name","hid");field2.put("type","keyword");// 英雄编号
        Map<String,String> field3 = new HashMap<>();field3.put("name","label");field3.put("type","keyWord");// 称号
        Map<String,String> field4 = new HashMap<>();field4.put("name","sex");field4.put("type","long");// 性别
        Map<String,String> field5 = new HashMap<>();field5.put("name","rank");field5.put("type","keyWord");// 军衔
        Map<String,String> field6 = new HashMap<>();field6.put("name","subjection");field6.put("type","keyword");//隶属
        Map<String,String> field7 = new HashMap<>();field7.put("name","intro");field7.put("type","text");// 简介
        Map<String,String> field8 = new HashMap<>();field8.put("name","cp");field8.put("type","keyword");// cp对象
        Map<String,String> field9 = new HashMap<>();field9.put("name","status");field9.put("type","keyword");// 神体状态
        List<Map<String,String>> columnList = Arrays.asList(field1,field2,field3,field4,field5,field6,field7,field8,field9);
        ESRestClientIndexUtil.createIndex("super_seminary","chaoshen",columnList);
    }
    @Test
    public void insertDoc(){
        String indexName = "super_seminary";
        List<Map<String,Object>> list = new ArrayList<>();
        list.add(loadMap("葛小伦 ","银河之力",1,null,"超神学院·地球防卫·雄兵连",null,"超神学院在地球基因实验中的第三代超级战士","进入超神学院前是一个整天想着拯救世界的人，加入学院后发掘了自身潜能，拥有媲美神体的硬度，是三大造神工程的最后一个——德星之力。第一季结尾展现了恐怖的潜力，第三季受彦的影响，长出了黑色翅膀，可以飞行。第一季倾心于杜蔷薇，但总得到模糊的答案，第三季随彦去了弗雷卓德星系，经常被彦挑逗。"));
        list.add(loadMap("赵信","德星之枪",1,null,"超神学院·地球防卫·雄兵连","天使右翼炙心","超神学院在地球基因实验中的第二代超级战。","长发飘飘，自诩祖上是赵云，地球人中最屌丝最逗逼的主角，没有之一。锲而不舍的追求蕾娜，屡战屡败，屡败屡战，虽然时常犯二，但总在关键时刻挺身而出（炮灰炮灰~），在作战中多次牵制对手为队友争取了不少时间。流老师说他将来会成为战斗力爆表的战士，但他觉得被忽悠了。第三季被莫甘娜利用，重创蕾娜，使蕾娜被莫甘娜控制。后遇到天使炙心，基因被增强，可以毁灭虚空战士。"));
        list.add(loadMap("程耀文",null,1,"上士","超神学院·地球防卫·雄兵连","蕾娜","第二代超级战士","德星王室光盾家族的子嗣，作为德诺星系的末代王者，对蕾娜心存芥蒂，在很长的一段时间里生活在梦魇中，逢熟人就说自己是王子，曾经倾心打抱不平的女警琪琳，但后来因身兼复兴家族王朝的使命，慢慢转移复兴国度上"));
        list.add(loadMap("杜蔷薇",null,0,"中士","超神学院·地球防卫·雄兵连",null,"由第二代超级战士升级为第三代超级战士","性格冷淡，掌握微虫洞技术，开学的时候让德班三基友吃尽苦头，热爱运动，对刘闯似乎有些过分崇拜，很多时候对殷勤的葛小伦不理不睬，冷嘲热讽，不过给葛小伦的答案总是模凌两可，耐人寻味。"));
        list.add(loadMap("琪琳",null,0,"中士","超神学院·地球防卫·雄兵连",null,"来自神河文明的第一代超级战士","天生热爱枪械，拥有超越常人的视野和狙击技术，与其说嫉恶如仇，不如说是唯恐天下而不乱，刚进入学校的第一天就弄得鸡犬不宁，种种举动在当时的程耀文眼里竟然是“对恶人很凶对人民很腼腆”，难道这是王子审核王后的标准？！在第三季第六集里表现非常出色，狙击掉多名恶魔，和野狼特战队配合成功营救出大D等人。"));
        list.add(loadMap("何蔚蓝",null,0,"下士","超神学院·地球防卫·雄兵连",null,"由第二代超级战士晋升为第三代超级战士","与琪琳同属神河文明的第一代超级战士，性格暴躁，一等一的女汉子，琪琳的发小，近身格斗专家，对刘闯之前做的坏事耿耿于怀，第三季第四集：“长城” 中对刘闯认识改变。目前正前往首都北之星。"));
        list.add(loadMap("蕾娜","太阳之光",0,null,"超神学院·地球防卫·雄兵连","程耀文","由第二代超级战士晋升为第三代超级战士","烈阳王唯一的年轻血脉，在洗澡的时候直接给流老师召唤传送到地球。　漂亮高贵，如同太阳一般耀眼的出场，就在屌丝横行的学院引起轩然大波，有些自傲，但在程耀文面前却抬不起头，祖辈的矛盾一直延续到两个人身上，属于初次见面以为很拽，时间久了发现很中二的御姐类型。对于身上存在“温柔”一词，Miss老师表示怀疑。第三季被莫甘娜控制间接干掉了凯莎，后来被扔在地球上，小伦从弗雷卓德星系回到地球时，从剑魔阿托手上就下的昏迷的小伦，并为其疗伤，在小伦昏迷时说：“对不起，小伦，我已经回不去了。”然后离开。"));
        list.add(loadMap("刘闯","诺星战神",1,"中士","超神学院·地球防卫·雄兵连",null,"神体","作为市井街头小混混的老大，在被招入超神学院后一直努力改邪归正，尽管常常受到队友的质疑和不理解，但还是默默地承受试着去补偿之前的过失，和瑞萌萌略聊得来，并且在多次作战中表现出色，弑神之力拥有者之一，诸神克星。三大造神工程之一。第三季中对莫甘娜的诱惑的拒绝，形象瞬间高大上。"));
        list.add(loadMap("瑞萌萌","诺星尖刀",0,"中士","超神学院·地球防卫·雄兵连",null,"第二代超级战士","单纯，善良，努力了却读不好书，供弟弟妹妹上学，兢兢业业为了让父母骄傲开心却碌碌无为，打过工饱受欺凌，懦弱的她加入超神学院后得到质的蜕变，当刘闯被蔚蓝质疑的时候第一个站了出来出头，当队友受伤战线被撕开崩溃时，因为自己帮不上忙而落泪。目前正前往首都北之星"));
        list.add(loadMap("左翼·彦","雷鸣战神",0,null,"天使星云·神圣护卫→天使统帅",null,"由第二代超级战士晋升为第三代超级战士","单纯，善良，努力了却读不好书，供弟弟妹妹上学，兢兢业业为了让父母骄傲开心却碌碌无为，打过工饱受欺凌，懦弱的她加入超神学院后得到质的蜕变，当刘闯被蔚蓝质疑的时候第一个站了出来出头，当队友受伤战线被撕开崩溃时，因为自己帮不上忙而落泪。目前正前往首都北之星"));
        list.add(loadMap("右翼·炙心","烈火战神",0,null,"天使星云·神圣护卫","赵信","第三代超级战士","第三季被凯莎女王许配给赵信，自身为虚空战士，改写了赵信的基因使其可以击败虚空战士，改写完之后，失去天使之力，自称不再是天使，没有了翅膀。只是是变成普通人了还是进化为完全的虚空战士还不得而知。"));
        list.add(loadMap("孙悟空","斗战胜佛",1,null,null,"苏小狸","第三代超级战士","沉睡于神话中的一段超级基因密码，接近神的超级战士，在这段基因密码中，吴老师偷偷植入了对阿狸的记忆。而后被死歌破解密码并将其召唤出来。"));
        list.add(loadMap("苏小狸",null,0,null,"超神学院·地球防务·雄兵连","孙悟空","第二代超级战士，擅长魅惑的战斗妖精","她不爱这个国家，不爱她的祖辈，不爱她的家人，就是他们，赐予她这一切，漫漫无常的等待，有人跟她说，让她全身充满魔力，给她找个伟岸的真男人。就这样，一个荒唐的交换条件，她加入了超神学院。"));
        list.add(loadMap("莫甘娜","堕落女王、自由与梦想之神",0,null,"恶魔星云",null,"由第二代超级战士晋升为第三代超级战士","同样来自古老强大的天使文明，不知名的原因出走天使星云，曾经是超神学院的导师，和基兰校长把酒言欢，探讨学术；后因理念不同再次出走，并且做了一生最大的转折，向自己的母星宣战，物是人非，此时天使星云的最高统治者就是自己曾经的姐姐凯莎，战争长达万年之久。"));
        list.add(loadMap("丁格黑",null,1,null,"神河文明",null,null,"生活在神河的伟大科学家，宇宙历史长河中的传说，从来没有被超越，时光神基兰和大时钟的创造者，一生致力于科学研究，虽然一直希望能够探索浩瀚无垠的宇宙，但是在神河星系爆炸的时候选择了一同湮灭。"));
        list.add(loadMap("泰·史奈夫",null,1,null,"弗雷卓文明·北方霸王",null,null,"处于冷兵器时代的费雷卓文明，正处在南北混战时期，而北方的各大蛮夷部落，他们各自信仰自己的神或图腾，其中的一只，史奈夫族，是北方名族最强大的一支。泰·史奈夫则是新一代的王者，但目前来看，除了为自己的信仰南征北战彰显蛮威，并没有太多成就。他们信仰的是时光神。"));
        list.add(loadMap("艾妮·熙德",null,0,null,"弗雷卓文明·冰雪王国",null,null,"处于冷兵器时代的费雷卓文明，参考《艾妮·熙德传奇》，从一介平民，和几个年轻的勇士一起，通过长达10年的战争，将南方各大城邦势力统一成一个最伟大的王国，而指引他们赢得胜利的正是审判天使的伟大信仰。他们在弗雷卓南部开创了新的骑士时代，将弗雷卓从野蛮带入了文明。（《艾妮·熙德传奇》将在不久后以超神学院系列之作登场。）"));
        list.add(loadMap("索顿","鳄神",1,null,null,null,"神级","自称是地球上的神，亦正亦邪，在异空间被莫甘娜破译召唤出来，可能时代过于久远，对这个新世界充满新鲜感，卖萌，搞笑，缓解气氛，搔首踟蹰，每次出场都是抢镜的亮点，据说还有个兄弟。"));
        list.add(loadMap("杜卡奥",null,1,"上将","超神学院·地球防务·雄兵连总指挥官",null,null,"杜蔷薇父亲，原诺星上将，发动过德诺星系的战争，战绩被载入宇宙史册，并在其他文明中广为流传，包括古老的天使文明之中。神河1000元年，把程耀文带到地球，并且是地球基因实验的策划者之一，监控着被挑选中的超级战士的一举一动。"));
        list.add(loadMap("钊",null,1,"中士","超神学院·地球防务·女神队",null,null,"持剑的超级战士，会使用所谓的无极剑道。目前和菲菲前往北之星。属于第二代超级战士"));
        list.add(loadMap("黑风",null,1,null,"恶魔星云",null,null,"莫甘娜曾经一手打造的梦境超级战士，用于从精神上打击天使集团的高能力者，也是跟随莫甘娜征战万年的老战友。"));
        list.add(loadMap("剑魔·阿托","上古剑魔",1,null,"恶魔星云",null,"由第二代超级战士升级为第三代超级战士","一个跟随了莫甘娜万年之久的老指挥官。在与雄兵连一战后，被莫甘娜培养成为弑神级战士，也是莫甘娜手下最强大的神级战士。为了自己的女王，可以轻易屠杀泰·史奈夫的族人，毫不手软。已进化为神体"));
        list.add(loadMap("卡尔","死神",1,null,"黑暗星云",null,null,"暗影世界的领袖，原超神学院的导师。专注于研究从超神时代打开终极恐惧之门，进入虚空世界，但仍然在诸神之间周旋。他坚持的理念，让曾经繁华的星球变成死亡之国。"));
        list.add(loadMap("凯莎","天使之王",0,null,"天使星云",null,"由第二代超级战士晋升为第三代超级战士","弘扬天使既正义的理念，几乎从信仰上统治了所有主流文明。天使星云的王者，宇宙已知文明的最高神。现已陨落。需要三忆年复活"));
        list.add(loadMap("杰古斯丁",null,1,null,"超神学院·地球防务·雄兵连",null,"第一代超级战士","超神学院 地球防务 雄兵连大队长。他是个杰出的指挥官，指挥过震惊世界的阿布拉拉行动，后被流老师召唤到地球，担任雄兵连的军事教官"));
        list.add(loadMap("语琴",null,0,null,"超神学院指挥部",null,"第一代超级战士","超神学院指挥部 医疗官。能够远程给受伤的战士进行修复，第三季中被恶魔劫持，状况不明。"));
        list.add(loadMap("怜风",null,0,null,"超神学院指挥部",null,"第一代超级战士","超神学院指挥部 情报分析员"));
        list.add(loadMap("魏颖","暗影猎手",0,null,"超神学院·地球防务·雄兵连",null,null,"弑神之力的另一个拥有者，德星第二代造神工程的产物，属于第二代超级战士其实力让莫甘娜十分忌惮。"));
        list.add(loadMap("李菲菲",null,0,"下士","超神学院·地球防务·雄兵连",null,"德星第二代超级战士",null));
        list.add(loadMap("潘震",null,1,null,"烈阳星",null,null,"超神学院 烈阳星 太阳神守护者"));

        for (Map<String, Object> bean : list) {
            ESRestClientIndexUtil.insertDoc(indexName,bean.get("hid").toString(),bean);
        }
    }
    @Test
    public void deleteDoc(){
        String indexName = "super_seminary";
        String esId = "890adc3c-ce73-496d-afda-1029ce438751";
        ESRestClientIndexUtil.deleteDoc(indexName,esId);
    }
    @Test
    public void updateDoc(){
        String indexName = "super_seminary";
        String esId = "890adc3c-ce73-496d-afda-1029ce438750";
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("name","葛小伦");
        ESRestClientIndexUtil.updateDoc(indexName,esId,jsonMap);
    }
    @Test
    public void getDoc(){
        String indexName = "super_seminary";
        String esId = "890adc3c-ce73-496d-afda-1029ce438750";
        Map<String, Object> doc = ESRestClientIndexUtil.getDoc(indexName, esId);
        log.info(doc.toString());
//        System.out.println(doc);
    }
    @Test
    public void reIndexTest(){
//        Map<String,String> field1 = new HashMap<>();field1.put("name","bd");field1.put("type","long");// 名称
//        Map<String,String> field2 = new HashMap<>();field2.put("name","ti");field2.put("type","keyword");// 英雄编号
//        Map<String,String> field3 = new HashMap<>();field3.put("name","apid");field3.put("type","keyWord");// 称号
//        Map<String,String> field4 = new HashMap<>();field4.put("name","apIn");field4.put("type","keyword");// 性别
//        Map<String,String> field5 = new HashMap<>();field5.put("name","rid");field5.put("type","keyWord");// 军衔
//        Map<String,String> field6 = new HashMap<>();field6.put("name","subjection");field6.put("type","keyword");//隶属
//        Map<String,String> field7 = new HashMap<>();field7.put("name","an");field7.put("type","text");// 简介
//        Map<String,String> field8 = new HashMap<>();field8.put("name","ap");field8.put("type","keyword");// cp对象
//        Map<String,String> field9 = new HashMap<>();field9.put("name","ed");field9.put("type","long");// 神体状态
//        List<Map<String,String>> columnList = Arrays.asList(field1,field2,field3,field4,field5,field6,field7,field8,field9);
//        ESRestClientIndexUtil.createIndex("new_zq","v1",columnList);

//        Map<String,String> field1 = new HashMap<>();field1.put("name","code");field1.put("type","long");// 名称
//        Map<String,String> field2 = new HashMap<>();field2.put("name","firstcode");field2.put("type","long");// 英雄编号
//        Map<String,String> field3 = new HashMap<>();field3.put("name","lvl");field3.put("type","long");// 称号
//        Map<String,String> field4 = new HashMap<>();field4.put("name","city");field4.put("type","keyword");// 性别
//        Map<String,String> field5 = new HashMap<>();field5.put("name","prv");field5.put("type","keyWord");// 军衔
//        Map<String,String> field6 = new HashMap<>();field6.put("name","cty");field6.put("type","keyword");//隶属
//        Map<String,String> field7 = new HashMap<>();field7.put("name","addr");field7.put("type","keyword");// 简介
//        Map<String,String> field8 = new HashMap<>();field8.put("name","type");field8.put("type","keyword");// cp对象
//        Map<String,String> field9 = new HashMap<>();field9.put("name","structure");field9.put("type","keyword");// 神体状态
//        List<Map<String,String>> columnList = Arrays.asList(field1,field2,field3,field4,field5,field6,field7,field8,field9);
//        ESRestClientIndexUtil.createIndex("cn_xz_area","area",columnList);
        ESRestClientIndexUtil.updateIndexSetting("tm_v2");
        ESRestClientIndexUtil.reIndexDoc(new String[]{"tm_v2"},"tm_v2","172.16.15.150",9200);
    }
    @Test
    public void createdAnalyzeIndex(){
//        String indexName = "tm_ap_test";
//        String alias = "ap";
//        Map<String,String> field1 = new HashMap<>();field1.put("name","apvu");field1.put("type","long");// 名称
//        Map<String,String> field2 = new HashMap<>();field2.put("name","clrq");field2.put("type","long");// 英雄编号
//        Map<String,String> field3 = new HashMap<>();field3.put("name","oid");field3.put("type","text");field3.put("analyzer","my_analyzer");// 称号
//        Map<String,String> field4 = new HashMap<>();field4.put("name","rid");field4.put("type","keyword");// 性别
//        Map<String,String> field5 = new HashMap<>();field5.put("name","jyfw");field5.put("type","keyWord");// 军衔
//        Map<String,String> field6 = new HashMap<>();field6.put("name","apovu");field6.put("type","long");//隶属
//        Map<String,String> field7 = new HashMap<>();field7.put("name","apEn");field7.put("type","text");field7.put("analyzer","uppercase");// 简介
//        Map<String,String> field8 = new HashMap<>();field8.put("name","gpjysc");field8.put("type","keyword");// cp对象
//        Map<String,String> field9 = new HashMap<>();field9.put("name","sbsl");field9.put("type","long");// 神体状态
//        Map<String,String> field10 = new HashMap<>();field10.put("name","cnty");field10.put("type","keyword");// 神体状态
//        Map<String,String> field11 = new HashMap<>();field11.put("name","zcjg");field11.put("type","keyword");// 神体状态
//        Map<String,String> field12 = new HashMap<>();field12.put("name","ctr");field12.put("type","keyword");// 神体状态
//        Map<String,String> field13 = new HashMap<>();field13.put("name","hzrq");field13.put("type","long");// 神体状态
//        Map<String,String> field14 = new HashMap<>();field14.put("name","apt");field14.put("type","long");// 神体状态
//        Map<String,String> field15 = new HashMap<>();field15.put("name","apid");field15.put("type","keyword");// 神体状态
//        Map<String,String> field16 = new HashMap<>();field16.put("name","cty");field16.put("type","keyword");// 神体状态
//        Map<String,String> field17 = new HashMap<>();field17.put("name","zczb");field17.put("type","double");// 神体状态
//        Map<String,String> field18 = new HashMap<>();field18.put("name","qygw");field18.put("type","keyword");// 神体状态
//        Map<String,String> field19 = new HashMap<>();field19.put("name","ap");field19.put("type","text");field19.put("analyzer","uppercase");// 神体状态
//        Map<String,String> field20 = new HashMap<>();field20.put("name","djzt");field20.put("type","keyword");// 神体状态
//        Map<String,String> field21 = new HashMap<>();field21.put("name","xydm");field21.put("type","keyword");
//        Map<String,String> field22 = new HashMap<>();field22.put("name","prv");field22.put("type","keyword");
//        Map<String,String> field23 = new HashMap<>();field23.put("name","svol");field23.put("type","keyword");
//        Map<String,String> field24 = new HashMap<>();field24.put("name","frdb");field24.put("type","keyword");
//        Map<String,String> field25 = new HashMap<>();field25.put("name","apad");field25.put("type","keyword");
//        List<Map<String,String>> columnList = Arrays.asList(field1,field2,field3,field4,field5,field6,field7,field8,field9,field10,field11,field12,
//                field13,field14,field15,field16,field17,field18,field19,field20,field21,field22,field23,field24,field25);
//        try {
//            // 先判断该索引名称是否已经存在
//            GetIndexRequest existsRequest = new GetIndexRequest(indexName);
//            boolean exists = RestClientHelper.getClient().indices().exists(existsRequest, RequestOptions.DEFAULT);
//            if (exists){
//                ESRestClientIndexUtil.deleteIndex(indexName);
//                System.out.println("索引["+indexName+"]已存在，创建失败~");
////                return;
//            }
//            // 映射索引的mapping
//            XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject().startObject("properties");
//            for (Map<String,String> column : columnList) {
//                if (!fieldTypes.contains(column.get("type").toLowerCase())) continue;
//                contentBuilder.startObject(column.get("name"));
//                String fieldType = column.get("type").toLowerCase();
//
//                if (Arrays.asList("byte","short","int","integer","long","date").contains(fieldType)){
//                    contentBuilder.field("type", "long").endObject();
//                }else if (Arrays.asList("float","double").contains(fieldType)){
//                    contentBuilder.field("type", "double").endObject();
//                }else if ("boolean".equals(fieldType)){
//                    contentBuilder.field("type", "boolean").endObject();
//                }else if ("string".equals(fieldType)) {
//                    contentBuilder.field("type", "keyword").endObject();
//                }else if ("keyword".equals(fieldType)) {
//                    contentBuilder.field("type", "keyword").endObject();
//                }else {
//                    if (column.containsKey("analyzer")){
//                        contentBuilder.field("type", "text");
//                        contentBuilder.field("analyzer",column.get("analyzer")).endObject();
//                    }else {
//                        contentBuilder.field("type", "text").endObject();
//                    }
//                }
//            }
//            contentBuilder.endObject().endObject();
//            // 创建索引
//            org.elasticsearch.client.indices.CreateIndexRequest request = new CreateIndexRequest(indexName);
//            // 配置索引相关的设置
//            request.settings(Settings.builder()
//                    .put("index.number_of_shards", 10)
//                    .put("index.number_of_replicas", 0)
//                    .put("index.max_result_window",10000000)
//                    // 配置自定义分词
//                    .put("index.analysis.analyzer.uppercase.filter","uppercase")
//                    .put("index.analysis.analyzer.uppercase.type","custom")
//                    .put("index.analysis.analyzer.uppercase.tokenizer","standard")
//                    .put("index.analysis.analyzer.my_analyzer.tokenizer","my_tokenizer")
//                    .put("index.analysis.tokenizer.my_tokenizer.pattern",";")
//                    .put("index.analysis.tokenizer.my_tokenizer.type","pattern")
//            );
//            request.mapping(contentBuilder);
//            if (!Strings.isNullOrEmpty(alias)) request.alias(new Alias(alias));
//            CreateIndexResponse response = RestClientHelper.getClient().indices().create(request, RequestOptions.DEFAULT);
//            boolean success = response.isAcknowledged();
//            System.out.println("是否创建成功?"+success);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        ESRestClientIndexUtil.reIndexDoc(new String[]{"tm_v2"},"tm_info","172.16.15.150",9200);
    }
    @Test
    public void getTest(){
        String indexName = "rest_01";
        String type = "type";
        String esId = "522f2bd9-81b7-4e69-aa1b-d3921e6b4298";
        GetResponse response = ESRestClientIndexUtil.get(indexName, esId);
        Map<String, Object> map = response.getSourceAsMap();
        System.out.println(map);
    }
    @Test
    public void searchTest(){
        String indexName = "tm_info";
        String type = "type";

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("grp","1201"));
//        boolQueryBuilder.must(QueryBuilders.termQuery("grp","3501"));
//        boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("ap","瑞萌舒乐工业公司"));
//        boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("apEn","INDUSTRIAS RAMON SOLER,S.A."));
//        boolQueryBuilder.minimumShouldMatch(1);
//        boolQueryBuilder.must(QueryBuilders.matchQuery("subjection","超神学院·地球防卫·雄兵连"));//match暂时无法查询，原因时es的服务器版本和客户端版本不一致，6.0.0的服务器缺少对auto_generate_synonyms_phrase_query的支持
//        boolQueryBuilder.must(QueryBuilders.boolQuery().should(QueryBuilders.wildcardQuery("intro.keyword","*地球*").boost(10)));
        SearchResponse response = ESRestClientIndexUtil.search(indexName, type, 0, 100, null, null, "vu", SortOrder.DESC, boolQueryBuilder);
        indexName = "tm_info_new";
        if (response!=null) {
            long total = response.getHits().getTotalHits().value;
            TotalHits.Relation relation = response.getHits().getTotalHits().relation;
            System.out.println(relation.name());
            System.out.println("总计查询结果数:" + total);
            List<SuperSeminary> list = ESRestClientIndexUtil.convert(SuperSeminary.class, response);
            list.forEach(System.out::println);
//            for (SearchHit hit : response.getHits().getHits()) {
//                System.out.println(hit.getSourceAsString());
//                System.out.println(hit.getSourceAsMap());
//                Map<String, Object> map = hit.getSourceAsMap();
//
//                map.put("grp",map.get("grp")==null?null:((String) map.getOrDefault("grp", "")).split(";"));
//                map.put("grpSim",map.get("grpSim")==null?null:((String) map.getOrDefault("grpSim", "")).split(";"));
//                map.put("grpSpfw",map.get("grpSpfw")==null?null:((String) map.getOrDefault("grpSpfw", "")).split(";"));
//                map.put("spdm",map.get("spdm")==null?null:((String) map.getOrDefault("spdm", "")).split(";"));
//                map.put("hyfl",map.get("hyfl")==null?null:((String) map.getOrDefault("hyfl", "")).split(";"));
//                map.put("txCc",map.get("txCc")==null?null:((String) map.getOrDefault("txCc", "")).split(";"));
//                map.put("encid",map.get("encid")==null?null:((String) map.getOrDefault("encid", "")).split(";"));
//                map.put("cid",map.get("cid")==null?null:((String) map.getOrDefault("cid", "")).split(";"));
//                map.put("oid",map.get("oid")==null?null:((String) map.getOrDefault("oid", "")).split(";"));
//                map.put("cncid",map.get("cncid")==null?null:((String) map.getOrDefault("cncid", "")).split(";"));
//                map.put("rrid",map.get("rrid")==null?null:((String) map.getOrDefault("rrid", "")).split(";"));
//                ESRestClientIndexUtil.insertDoc(indexName,hit.getId(),map);
//            }
        }
    }
    @Test
    public void aggregationsTest(){
        String indexName = "tm_ap_test";
        String type = "type";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(QueryBuilders.termQuery("agid","AG19916"));
        Map<Object, Long> count = ESRestClientIndexUtil.count(indexName, type, 10, null, "ap", boolQueryBuilder);
        System.out.println("统计结果: " + count);
        Map<String, LinkedHashMap<Object, Long>> count1 = ESRestClientIndexUtil.count(indexName, type, 20,  new String[]{"ap","apEn"}, boolQueryBuilder);
//        System.out.println("count:"+count);
        System.out.println(count1);
//        ESRestClientIndexUtil.max(indexName, type, "apvu", boolQueryBuilder);
//        ESRestClientIndexUtil.min(indexName, type, "apvu", boolQueryBuilder);
//        ESRestClientIndexUtil.avg(indexName, type, "apvu", boolQueryBuilder);
//        ESRestClientIndexUtil.sum(indexName, type, "apvu", boolQueryBuilder);
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
    private Map<String,Object> loadMap(String name, String label, int sex, String rank, String subjection, String cp, String status, String intro){
        Map<String,Object> result = new HashMap<>();
        result.put("name",name);
        result.put("hid",UUID.randomUUID().toString().toLowerCase());
        result.put("label",label);
        result.put("sex",sex);
        result.put("rank",rank);
        result.put("subjection",subjection);
        result.put("cp",cp);
        result.put("status",status);
        result.put("intro",intro);
        return result;
    }
}
