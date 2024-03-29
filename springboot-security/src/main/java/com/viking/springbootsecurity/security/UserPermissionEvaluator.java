package com.viking.springbootsecurity.security;

import com.viking.springbootsecurity.security.entity.SelfUserEntity;
import com.viking.springbootsecurity.core.entity.SysPermissionsEntity;
import com.viking.springbootsecurity.core.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *自定义权限注解验证
 */

@Component
public class UserPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private SysUserService sysUserService;

    /**
     * * hasPermission鉴权方法
     * 这里仅仅判断PreAuthorize注解中的权限表达式
     * 实际中可以根据业务需求设计数据库通过targetUrl和permission做更复杂鉴权
     * 当然targetUrl不一定是URL可以是数据Id还可以是管理员标识等,这里根据需求自行设计
     * @param authentication 用户身份(在使用hasPermission表达式时Authentication参数默认会自动带上)
     * @param targetUrl 请求路径
     * @param permission 请求路径权限
     * @return boolean 是否通过
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object permission) {
        // 获取用户信息
        SelfUserEntity selfUserEntity = (SelfUserEntity) authentication.getPrincipal();
        // 查询用户权限(这里可以将权限放入缓存中提升效率)
        Set<String> permissions = new HashSet<>();
        List<SysPermissionsEntity> sysMenuEntityList = sysUserService.selectSysPermissionsByUserId(selfUserEntity.getUserId());
        for (SysPermissionsEntity sysMenuEntity:sysMenuEntityList) {
            permissions.add(sysMenuEntity.getPermissions());
        }
        // 权限对比
        if (permissions.contains(permission.toString())){
            return true;
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {

        return false;
    }
}
