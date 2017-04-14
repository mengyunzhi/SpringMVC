---
layout: post
title:  "第四节：初始化单元测试 -- service"
description: "根据图表初始化单元测试内容"
date: 2017-04-13T15:15:30+08:00
categories: chapter3
author: "潘杰"
---
有了时序图，也有了基本的代码。下一步，我们需要做的并**不是**开始去编写`service`的实现类，而是先写测试用例。

> 在初期的阶段，由于我们对`SpringMVC`的掌握程度较低，往往是先编写实现类，再编写测试用例。

# 生成测试类
`idea`为我们提供了快速生成测试类的快捷操作。

![{{site.imageurl}}/chapter3/2.gif]({{site.imageurl}}/chapter3/2.gif)

> 我们直接建立了`KlassService`的测试类，而不是`KlassServiceImpl`的测试类，这是为什么呢？

测试类生成后，我们加入`SpringMVC`的测试注解后代码如下：
{% highlight java %}
package com.mengyunzhi.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by panjie on 17/4/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KlassServiceTest {
    @Test
    public void save() throws Exception {

    }

    @Test
    public void get() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

}
{% endhighlight %}

## saveTest
和以前的方法相同，我们先要做的是写注释，再然后按注释写入代码
{% highlight java %}
    @Test
    public void save() throws Exception {
        // 新增教师
        
        // 新增班级

        // 断言返回值非NULL
    }
{% endhighlight %}

代码：
{% highlight java %}
    @Test
    public void save() throws Exception {
        // 新增教师
        Teacher teacher = new Teacher(
                "zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                true);
        teacherRepository.save(teacher);

        // 新增班级
        Klass klass = klassService.save("软件班", teacher.getId());

        // 断言返回值非NULL
        assertThat(klass).isNotNull();
    }
{% endhighlight %}


## getTest
{% highlight java %}
    @Test
    public void get() throws Exception {
        // 断言查询ID为0的结果为null
        assertThat(klassService.get(10000L)).isNull();

        // 新增教师
        Teacher teacher = new Teacher(
                "zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                true);
        teacherRepository.save(teacher);

        // 新增班级
        Klass klass = klassService.save("软件班", teacher.getId());

        // 断言查询到的新增的的ID不为null
        assertThat(klassService.get(klass.getId())).isNotNull();
    }
{% endhighlight %}

当我们写完这个测试方法，我们发现竟然有这么多的重复代码。是的，如果我们现在不重构的这样写下去，还有会更多的冗余代码，就是由于数据`CRUD`，都是基本`C`这个操作的。下面，我们使用`@Before`注解来解决代码冗余的问题。

{% highlight java %}
    private Klass klass;

    @Before
    public void before() {
        // 新增教师
        Teacher teacher = new Teacher(
                "zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                true);
        teacherRepository.save(teacher);

        // 新增班级
        klass = klassService.save("软件班", teacher.getId());
    }

    @Test
    public void save() throws Exception {
        // 断言返回值非NULL
        assertThat(klass).isNotNull();
    }

    @Test
    public void get() throws Exception {
        // 断言查询ID为0的结果为null
        assertThat(klassService.get(10000L)).isNull();

        // 断言查询到的新增的的ID不为null
        assertThat(klassService.get(klass.getId())).isNotNull();
    }

{% endhighlight %}
上述代码中，我们将代码重复的部分，移至了`Before`方法，并增加`@Before`注解，意为:在执行每个测试前，都执行一遍这个注解中的内容。

## deleteTest
{% highlight java %}
    @Test
    public void delete() throws Exception {
        // 查询实体，断言其存在

        // 将实体删除

        // 查询实体，断言不存在
    }
{% endhighlight %}

加入代码:
{% highlight java %}
    @Test
    public void delete() throws Exception {
        // 查询实体，断言其存在
        assertThat(klass).isNotNull();

        // 将实体删除
        klassService.delete(klass.getId());

        // 查询实体，断言其不存在
        assertThat(klassService.get(klass.getId())).isNull();
    }
{% endhighlight %}

## updateTest
{% highlight java %}
    @Test
    public void update() throws Exception {
        // 新建一个教师实体

        // 更新

        // 查询, 并断言
    }
{% endhighlight %}

加入代码：
{% highlight java %}
    @Test
    public void update() throws Exception {
        // 新建一个教师实体
        Teacher teacher = new Teacher(
                "zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                true);
        teacherRepository.save(teacher);

        // 更新
        String name = "软件工程";
        klassService.update(klass.getId(), name, teacher.getId());

        // 查询, 并断言
        Klass newKlass = klassService.get(klass.getId());
        assertThat(newKlass.getName()).isEqualTo(name);
        assertThat(newKlass.getTeacher().getId()).isEqualTo(teacher.getId());
    }
{% endhighlight %}
    
<hr />
是的，对于基本的`CRUD`操作，这样做的确有些过了。我们在教程中强调的单元测试，是让大家养成好的单元测试的习惯，把它做为开发的必需条件，有代码，就有测试。这样当我们以后涉及到越来越多的，逻辑运算的时候，便会因良好的测试习惯而事半功倍了。


