package com.viking.springbootsecurity.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viking.springbootsecurity.core.dao.SysUserRoleDao;
import com.viking.springbootsecurity.core.entity.SysUserRoleEntity;
import com.viking.springbootsecurity.core.service.SysUserRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRoleEntity> implements SysUserRoleService {
}
