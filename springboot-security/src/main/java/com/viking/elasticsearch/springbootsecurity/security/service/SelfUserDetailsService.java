package com.viking.springbootsecurity.security.service;

import com.viking.springbootsecurity.core.entity.SysUserEntity;
import com.viking.springbootsecurity.core.service.SysUserService;
import com.viking.springbootsecurity.security.entity.SelfUserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SelfUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 查询用户信息
     * @param userName  用户名
     * @return UserDetails SpringSecurity用户信息
     * @throws UsernameNotFoundException 用户名未匹配异常
     */
    @Override
    public SelfUserEntity loadUserByUsername(String userName) throws UsernameNotFoundException {
        //查询用户信息
        SysUserEntity sysUserEntity = sysUserService.selectUserByName(userName);
        if (sysUserEntity!=null){
            //组装参数
            SelfUserEntity selfUserEntity = new SelfUserEntity();
            BeanUtils.copyProperties(sysUserEntity,selfUserEntity);
            return selfUserEntity;
        }
        return null;
    }
}
