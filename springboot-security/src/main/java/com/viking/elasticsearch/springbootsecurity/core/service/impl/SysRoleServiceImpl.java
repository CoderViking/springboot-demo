package com.viking.springbootsecurity.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viking.springbootsecurity.core.dao.SysRoleDao;
import com.viking.springbootsecurity.core.entity.SysRoleEntity;
import com.viking.springbootsecurity.core.service.SysRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
}
