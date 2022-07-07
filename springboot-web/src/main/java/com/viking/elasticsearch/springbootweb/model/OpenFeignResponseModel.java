package com.viking.elasticsearch.springbootweb.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Viking on 2022/7/7
 * openFeign返回值测试
 */
@Data
public class OpenFeignResponseModel implements Serializable {
    private String msg ;
    private Integer status;
    private Map<String, Object> data;
}
