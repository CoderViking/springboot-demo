package com.viking.elasticsearch.springbootsecurity;

import com.viking.elasticsearch.springbootsecurity.core.entity.SysUserEntity;
import com.viking.elasticsearch.springbootsecurity.core.entity.SysUserRoleEntity;
import com.viking.elasticsearch.springbootsecurity.core.service.SysUserRoleService;
import com.viking.elasticsearch.springbootsecurity.core.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class SpringbootSecurityApplicationTests {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SysUserRoleService sysUserRoleService;


    /**
     * 注册用户
     */
    @Test
    public void contextLoads() {
        // 注册用户
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUsername("viking");
        sysUserEntity.setPassword(bCryptPasswordEncoder.encode("123456"));
        // 设置用户状态
        sysUserEntity.setStatus("NORMAL");
        sysUserService.save(sysUserEntity);
        // 分配角色 1:ADMIN 2:USER
        SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
        sysUserRoleEntity.setRoleId(2L);
        sysUserRoleEntity.setUserId(sysUserEntity.getUserId());
        sysUserRoleService.save(sysUserRoleEntity);
    }
//    @Test
//    void contextLoads() {
//    }

}
