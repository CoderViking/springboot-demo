package com.viking.elasticsearch.springbootsecurity.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viking.elasticsearch.springbootsecurity.core.dao.SysPermissionsDao;
import com.viking.elasticsearch.springbootsecurity.core.entity.SysPermissionsEntity;
import com.viking.elasticsearch.springbootsecurity.core.service.SysPermissionsService;
import org.springframework.stereotype.Service;

@Service
public class SysPermissionsServiceImpl extends ServiceImpl<SysPermissionsDao, SysPermissionsEntity> implements SysPermissionsService {
}
