package com.viking.mybatisplus.config.druid;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * Created by Viking on 2019/5/7
 * 配置DruidStatViewServlet
 */
@WebServlet(
        urlPatterns= {DruidConfiguration.URL_PATTERNS},
        initParams= {
                @WebInitParam(name="allow",value= DruidConfiguration.ALLOW_HOST),//允许访问HTML页面的IP
                @WebInitParam(name="loginUsername",value= DruidConfiguration.USERNAME),//登录用户名
                @WebInitParam(name="loginPassword",value= DruidConfiguration.PASSWORD),//登录密码
                @WebInitParam(name="resetEnable",value= DruidConfiguration.RESET_ENABLE)// 允许HTML页面上的“Reset All”功能
        }
)
public class DruidStatViewServlet extends StatViewServlet implements Servlet {
    private static final long serialVersionUID = 1L;
}
