---
layout: post
title:  "第九节：更新数据 -- 对接C层"
description: "C层与service对接"
date: 2017-04-07T15:25:59+08:00
categories: chapter2
author: "潘杰"
---
以数据更新为例，在`SpringMVC`的世界里，大体的操作是这样的：
![springmvc seq]({{site.imageurl}}/chapter2/29.png)

`Spring`为我们自动实现了`TeacherRepository`接口，我们手动的实现了`TeacherService`接口，并进行单元测试。在以上两个接口可用并测试的基础上，我们的C层所要做的工作就是，直接将数据转发给`service`。
{% highlight java %}
    // @PutMapping 表明该方法只接收 put 请求.
    @PutMapping("/{id}")
    public Teacher updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) throws EntityNotFoundException {
        // 数据转发
        return teacherService.saveTeacher(id, teacher);
    }
{% endhighlight %}

由于`teacherService`在执行`saveTeacher`时，可能会抛出一个异常。我们在这里，可以手动的使用`try catch`来进行处理后，重新定制一个新的异常，返回给`spring`，也可以在函数声明中，加入`throws EntityNotFoundException`来直接抛出这个异常。在这里，我们直接将异常向上抛出。

完整代码：
{% highlight java %}
package com.mengyunzhi.controller;

import com.mengyunzhi.repository.Teacher;
import com.mengyunzhi.repository.TeacherRepository;
import com.mengyunzhi.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

/**
 * Created by panjie on 17/4/6.
 */
// 声明为Rest控制器（支持前后台分离）
@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherService teacherService;

    // 设置路由
    @PostMapping("/save")
    // 使用@RequestBody注解，将请求的`json`数据，直接加载至teacher对象
    public Teacher saveTeacher(@Valid @RequestBody Teacher teacher) {
        // 打印加载的数据
        System.out.println(teacher);

        // 调用保存操作
        return teacherRepository.save(teacher);
    }

    // @GetMapping 表明该方法只接收 get 请求.
    // {id}即为url中传入教师关键字
    @GetMapping("/{id}")
    // @PathVariable 获取路由中的id值
    public Teacher getTeacherById(@PathVariable Long id) {
        return teacherRepository.findOne(id);
    }

    // @PutMapping 表明该方法只接收 put 请求.
    @PutMapping("/{id}")
    public Teacher updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) throws EntityNotFoundException {
        // 数据转发
        return teacherService.saveTeacher(id, teacher);
    }
}
{% endhighlight %}
## 测试
正确用例，略。
错误用例：
![update error unit test]({{site.imageurl}}/chapter2/30.png)

## 作业
将数据增加与删除，由直接调用`repository`改写为调用`service`。