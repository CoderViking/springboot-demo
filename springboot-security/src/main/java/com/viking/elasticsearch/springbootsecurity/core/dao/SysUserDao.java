package com.viking.springbootsecurity.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.viking.springbootsecurity.core.entity.SysPermissionsEntity;
import com.viking.springbootsecurity.core.entity.SysRoleEntity;
import com.viking.springbootsecurity.core.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {
    List<SysRoleEntity> selectSysRoleByUserId(Long userId);
    List<SysPermissionsEntity> selectSysPermissionsByUserId(Long userId);
}
