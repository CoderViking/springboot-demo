package com.viking.springbootsecurity.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viking.springbootsecurity.core.dao.SysPermissionsDao;
import com.viking.springbootsecurity.core.entity.SysPermissionsEntity;
import com.viking.springbootsecurity.core.service.SysPermissionsService;
import org.springframework.stereotype.Service;

@Service
public class SysPermissionsServiceImpl extends ServiceImpl<SysPermissionsDao, SysPermissionsEntity> implements SysPermissionsService {
}
