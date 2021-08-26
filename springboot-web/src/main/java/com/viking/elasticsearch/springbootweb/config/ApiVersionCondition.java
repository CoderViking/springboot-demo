package com.viking.elasticsearch.springbootweb.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created By Viking on 2021/5/31
 */
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {
    private static Logger log = LoggerFactory.getLogger(ApiVersionCondition.class);
    //类中的成员变量，此处表示到时候接口中传递的参数接收
    private int apiVersion;

    public ApiVersionCondition(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    public int getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    // 路径中版本的前缀， 这里用 /v[1-9]/的形式
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/");

    //创建新的实例
    @Override
    public ApiVersionCondition combine(ApiVersionCondition apiVersionCondition) {
        return new ApiVersionCondition(apiVersionCondition.getApiVersion());
    }

    //校验请求url中是否包含指定的字段，如果存在则进行正则匹配
    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest httpServletRequest) {
        log.info("---- getMatchingCondition ----");
        Matcher m = VERSION_PREFIX_PATTERN.matcher(httpServletRequest.getRequestURI());
        if(m.find()){
            int version = Integer.parseInt(m.group(1));
            log.info("---- getMatchingCondition ----version="+ version);
            //如果当前url中传递的版本信息高于(或等于)申明(或默认)版本，则用url的版本
            if(version >= this.apiVersion){
                return this;
            }
        }
        //不匹配，则不用管
        return null;

    }

    //重写比较方法
    @Override
    public int compareTo(ApiVersionCondition apiVersionCondition, HttpServletRequest httpServletRequest) {
        // 优先匹配最新的版本号
        return apiVersionCondition.getApiVersion() - this.apiVersion;
    }
}
