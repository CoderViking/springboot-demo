package com.viking.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;

/**
 * Created By Viking on 2021/9/27
 * 自定义sessionFactory会话
 */
public class OnlineSessionFactory implements SessionFactory {
    @Override
    public Session createSession(SessionContext sessionContext) {

        return null;
    }
}
