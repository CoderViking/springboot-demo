package com.viking.elasticsearch.springbootsecurity.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.viking.elasticsearch.springbootsecurity.core.entity.SysRolePermissionsEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysRolePermissionsDao extends BaseMapper<SysRolePermissionsEntity> {
}
