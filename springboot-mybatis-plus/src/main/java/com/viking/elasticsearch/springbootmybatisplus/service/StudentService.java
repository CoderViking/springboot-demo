package com.viking.springbootmybatisplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.viking.springbootmybatisplus.entity.Student;

/**
 * Created by Viking on 2019/9/17
 */
public interface StudentService extends IService<Student>{
    Student getStudentById(Long id);
}
