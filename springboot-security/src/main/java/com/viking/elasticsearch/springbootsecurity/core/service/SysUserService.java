package com.viking.elasticsearch.springbootsecurity.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.viking.elasticsearch.springbootsecurity.core.entity.SysPermissionsEntity;
import com.viking.elasticsearch.springbootsecurity.core.entity.SysRoleEntity;
import com.viking.elasticsearch.springbootsecurity.core.entity.SysUserEntity;

import java.util.List;

public interface SysUserService extends IService<SysUserEntity> {

    SysUserEntity selectUserByName(String userName);

    List<SysRoleEntity> selectSysRoleByUserId(Long userId);

    List<SysPermissionsEntity> selectSysPermissionsByUserId(Long userId);
}
