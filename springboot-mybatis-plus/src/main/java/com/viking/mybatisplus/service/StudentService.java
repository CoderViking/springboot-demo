package com.viking.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.viking.mybatisplus.entity.Student;

/**
 * Created by Viking on 2019/9/17
 */
public interface StudentService extends IService<Student>{
    Student getStudentById(Long id);

    Page<Student> selectPageByCustomSql(IPage<Student> page, QueryWrapper<Student> queryWrapper);
}
