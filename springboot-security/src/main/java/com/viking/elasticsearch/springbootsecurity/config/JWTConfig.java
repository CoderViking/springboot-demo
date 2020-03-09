package com.viking.springbootsecurity.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTConfig {
    //密钥KEY
    public static String secret;
    //TokenKey
    public static String tokenHeader;
    //Token前缀字符
    public static String tokenPrefix;
    //过期时间
    public static Integer expiration;
    //不需要认证的接口
    public static String antMatchers;

    public static void setSecret(String secret) {
        JWTConfig.secret = secret;
    }

    public static void setTokenHeader(String tokenHeader) {
        JWTConfig.tokenHeader = tokenHeader;
    }

    public static void setTokenPrefix(String tokenPrefix) {
        JWTConfig.tokenPrefix = tokenPrefix;
    }

    public static void setExpiration(Integer expiration) {
        JWTConfig.expiration = expiration*1000;
    }

    public static void setAntMatchers(String antMatchers) {
        JWTConfig.antMatchers = antMatchers;
    }
}
