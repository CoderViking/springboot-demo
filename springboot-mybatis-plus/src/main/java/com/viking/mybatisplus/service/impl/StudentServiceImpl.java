package com.viking.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viking.mybatisplus.entity.Student;
import com.viking.mybatisplus.dao.StudentMapper;
import com.viking.mybatisplus.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by Viking on 2019/9/17
 * 这里我们看到,service中我们没有写任何方法,
 * MyBatis-Plus官方封装了许多基本CRUD的方法,
 * 可以直接使用大量节约时间,
 * MP共通方法详见IService,ServiceImpl,BaseMapper源码,
 * 写入操作在ServiceImpl中已有事务绑定,
 * 这里我们举一些常用的方法演示.
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Autowired
    private StudentMapper mapper;

    @Override
    public Student getStudentById(Long id){
        return mapper.selectById(id);
    }
}
