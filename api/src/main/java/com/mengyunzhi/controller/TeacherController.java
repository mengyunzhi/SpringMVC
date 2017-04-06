package com.mengyunzhi.controller;

import com.mengyunzhi.repository.Teacher;
import com.mengyunzhi.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by panjie on 17/4/6.
 */
// 声明为Rest控制器（支持前后台分离）
@RestController
public class TeacherController {
    @Autowired
    private TeacherRepository teacherRepository;

    // 设置路由
    @RequestMapping("/teacher/save")
    // 使用@RequestBody注解，将请求的`json`数据，直接加载至teacher对象
    public Teacher saveTeacher(@Valid @RequestBody Teacher teacher) {
        // 打印加载的数据
        System.out.println(teacher);

        // 调用保存操作
        return teacherRepository.save(teacher);
    }
}
