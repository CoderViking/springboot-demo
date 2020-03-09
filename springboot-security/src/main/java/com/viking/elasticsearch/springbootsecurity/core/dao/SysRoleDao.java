package com.viking.springbootsecurity.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.viking.springbootsecurity.core.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {
}
