package com.viking.shiro.web.session;

import org.apache.shiro.session.mgt.SessionValidationScheduler;

/**
 * Created By Viking on 2021/9/27
 * 自定义任务调度器
 */
public class SpringSessionValidationScheduler implements SessionValidationScheduler {

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void enableSessionValidation() {

    }

    @Override
    public void disableSessionValidation() {

    }
}
