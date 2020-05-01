package com.viking.mybatisplus.Controller;

import com.viking.mybatisplus.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Viking on 2020/5/1
 */
@RestController
@RequestMapping("local")
public class LocalController {

    private final StudentService studentService;

    public LocalController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("connect")
    public Object connectLocal(String id){
        System.out.println("id:" + id);
        return "Connect to local success!";
    }

    @GetMapping("student")
    public Object getStudent(Long id){
        return studentService.getStudentById(id);
    }

}
