package com.viking.springbootsecurity.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.viking.springbootsecurity.core.entity.SysPermissionsEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysPermissionsDao extends BaseMapper<SysPermissionsEntity> {
}
