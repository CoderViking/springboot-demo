package com.viking.elasticsearch.springbootsecurity.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viking.elasticsearch.springbootsecurity.core.dao.SysRoleDao;
import com.viking.elasticsearch.springbootsecurity.core.entity.SysRoleEntity;
import com.viking.elasticsearch.springbootsecurity.core.service.SysRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
}
