package com.viking.elasticsearch.springbootsecurity.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viking.elasticsearch.springbootsecurity.core.dao.SysRolePermissionsDao;
import com.viking.elasticsearch.springbootsecurity.core.entity.SysRolePermissionsEntity;
import com.viking.elasticsearch.springbootsecurity.core.service.SysRolePermissionsService;
import org.springframework.stereotype.Service;

@Service
public class SysRolePermissionsServiceImpl extends ServiceImpl<SysRolePermissionsDao, SysRolePermissionsEntity>
        implements SysRolePermissionsService {
}
