package com.viking.web.ansj;

import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

/**
 * 分词器测试
 * Created By yanshuai on 2020/4/1
 */
public class AnalysisTest {

    @Test
    public void test(){
        String str = "欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!" ;
        str = "米其林美的生活";
        System.out.println(ToAnalysis.parse(str));
    }
}
