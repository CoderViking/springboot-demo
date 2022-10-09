package com.viking.springbootsecurity.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viking.springbootsecurity.core.dao.SysUserDao;
import com.viking.springbootsecurity.core.entity.SysPermissionsEntity;
import com.viking.springbootsecurity.core.entity.SysRoleEntity;
import com.viking.springbootsecurity.core.entity.SysUserEntity;
import com.viking.springbootsecurity.core.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService  {

    /**
     * 根据用户名查询用户实体
     * @param userName 用户名
     * @return SysUserEntity 用户实体
     */
    @Override
    public SysUserEntity selectUserByName(String userName) {
        QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserEntity :: getUsername,userName);
        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 通过用户ID查询角色集合
     * @param userId 用户ID
     * @return List<SysRoleEntity> 角色集合
     */
    @Override
    public List<SysRoleEntity> selectSysRoleByUserId(Long userId) {
        return this.baseMapper.selectSysRoleByUserId(userId);
    }

    /**
     * 根据用户ID查询权限集合
     * @param userId 用户ID
     * @return List<SysPermissionsEntity> 角色名集合
     */
    @Override
    public List<SysPermissionsEntity> selectSysPermissionsByUserId(Long userId) {
        return this.baseMapper.selectSysPermissionsByUserId(userId);
    }
}
