package com.viking.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Created by Viking on 2019/9/17
 */
@Data
@TableName("student")//@TableName中的值对应着表名
public class Student {
    /**
     * 主键
     * @TableId中可以决定主键的类型,不写会采取默认值,默认值可以在yml中配置
     * AUTO: 数据库ID自增
     * INPUT: 用户输入ID
     * ID_WORKER: 全局唯一ID，Long类型的主键
     * ID_WORKER_STR: 字符串全局唯一ID
     * UUID: 全局唯一ID，UUID类型的主键
     * NONE: 该类型为未设置主键类型
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 技能
     */
    private String skill;
    /**
     * 评价
     */
    private String evaluate;
    /**
     * 分数
     */
    private Long fraction;

}
