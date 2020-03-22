package com.viking.elasticsearch.entity;

import lombok.Data;
import lombok.ToString;

/**
 *
 * Created by Viking on 2020/3/22
 */
@Data
@ToString
public class SuperSeminary {
    private String name;// 名称
    private String hid;// 英雄编号
    private String label;// 称号
    private int sex;// 性别
    private String rank;// 军衔
    private String subjection;//隶属
    private String cp;// cp对象
    private String status;// 神体状态
    private String intro;// 简介
}
