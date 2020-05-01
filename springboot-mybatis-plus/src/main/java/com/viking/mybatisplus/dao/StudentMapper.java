package com.viking.mybatisplus.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.viking.mybatisplus.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Viking on 2019/9/17
 */
public interface StudentMapper extends BaseMapper<Student> {

    // 自定义sql和QueryWrapper完美结合使用,（也可以将sql写入xml文件中）
    @Select("select *,year(birth) as birth_year from STUDENT ${ew.customSqlSegment}")
    Page<Student> selectPageByCustomSql(IPage<Student> page, @Param(Constants.WRAPPER)QueryWrapper<Student> queryWrapper);
}
