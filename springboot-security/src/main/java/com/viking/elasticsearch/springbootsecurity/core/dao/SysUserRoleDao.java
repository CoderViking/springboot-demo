package com.viking.elasticsearch.springbootsecurity.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.viking.elasticsearch.springbootsecurity.core.entity.SysUserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserRoleDao extends BaseMapper<SysUserRoleEntity> {
}
