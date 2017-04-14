---
layout: post
title:  "第五节：代码开发 -- service"
description: "进行实现类的代码开发，并最终通过测试用例"
date: 2017-04-13T16:10:24+08:00
categories: chapter3
author: "潘杰"
---
本节中，我们共同开实现`KlassService`接口，并实现其功能，最终通过所有的测试用例。使用`idea`默认为我们生成的实现类代码如下:

{% highlight java %}
package com.mengyunzhi.service;

import com.mengyunzhi.repository.Klass;

/**
 * Created by panjie on 17/4/13.
 */
public class KlassServiceImpl implements KlassService {
    @Override
    public Klass save(String name, Long teacherId) {
        return null;
    }

    @Override
    public Klass get(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Klass update(Long id, String name, Long teacherId) {
        return null;
    }
}
{% endhighlight %}

在此基础上，我们增加`@Service`注解，以使`SpringMVC`我们自动实例化，并注入到相应类中。
{% highlight java %}
@Service
public class KlassServiceImpl implements KlassService {
{% endhighlight %}

## save
{% highlight java %}
    @Autowired
    private KlassRepository klassRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public Klass save(String name, Long teacherId) {
        // 实例化，并设置班级名称
        Klass klass = new Klass();
        klass.setName(name);

        // 获取教师
        Teacher teacher = teacherRepository.findOne(teacherId);

        // 设置教师
        klass.setTeacher(teacher);

        // 保存
        return klassRepository.save(klass);
    }
{% endhighlight %}


测试：

![{{site.imageurl}}/chapter3/19.png]({{site.imageurl}}/chapter3/19.png)

## get
{% highlight java %}
    @Override
    public Klass get(Long id) {
        return klassRepository.findOne(id);
    }
{% endhighlight %}

测试：
![{{site.imageurl}}/chapter3/20.png]({{site.imageurl}}/chapter3/20.png)

## delete
{% highlight java %}
    @Override
    public void delete(Long id) {
        klassRepository.delete(id);
        return;
    }
{% endhighlight %}

测试：
![{{site.imageurl}}/chapter3/21.png]({{site.imageurl}}/chapter3/21.png)

## update
{% highlight java %}
    @Override
    public Klass update(Long id, String name, Long teacherId) {
        // 实例化班级，赋班级名称
        Klass klass = klassRepository.findOne(id);
        klass.setName(name);

        // 实例化传入的教师，设置教师
        Teacher teacher = teacherRepository.findOne(teacherId);
        klass.setTeacher(teacher);

        return klassRepository.save(klass);
    }
{% endhighlight %}

![{{site.imageurl}}/chapter3/22.png]({{site.imageurl}}/chapter3/22.png)

# 测试全部用例

![{{site.imageurl}}/chapter3/23.png]({{site.imageurl}}/chapter3/23.png)

当我们看到这个成绩时，就可以自豪的进行`pull request`了。

<hr />
# 完善代码
我们刚刚在实例化一个班级时，由于其与教师存在关联关系，所以，我们需要在实例化班级以前，首先实例化一个教师实体。显得的，随着我们的数据表关联越来越多，越来越复杂。这种做法将越来越难以维护。

为此，为了便于测试，我们在`TeacherSerice`及`KlassService`中，分别增加一个获取一个新实例的方法。

`KlassService`
{% highlight java %}
public interface KlassService {
    Klass save(String name, Long teacherId);
    Klass get(Long id);
    void delete(Long id);
    Klass update(Long id, String name, Long teacherId);
    Klass getNewKlass();    // 获取一个新的教师实例
}
{% endhighlight %}

`TeacherSerice`
{% highlight java %}
public interface TeacherService {
    /**
     * 保存
     * @param id 关键字
     * @param teacher 教师
     * @return 保存后的教师
     */
    Teacher saveTeacher(Long id, Teacher teacher) throws EntityNotFoundException;
    void deleteTeacher(Teacher teacher);    // 删除实体
    void deleteTeacherById(Long id);        // 删除实体
    Teacher getNewTeacher();                // 获取一个新的教师实例
}

{% endhighlight %}

## 单元测试
然后我们分别期待这两个方法应该返回一个非`null`的的实体，据此，我们写两个单元测试:

由于历史原因，我们有以下两个测试类，分别对应`KlassService`及`KlassServerImpl`的测试。以下代码，便是分别这入这两上测试类中的。


![{{site.imageurl}}/chapter3/24.png]({{site.imageurl}}/chapter3/24.png)

断言返回的对象非`null`
{% highlight java %}
    @Test
    public void getNewTeacher() {
        assertThat(teacherService.getNewTeacher()).isNotNull();
    }
{% endhighlight %}

断言返回的对象非`null`
{% highlight java %}
    @Test
    public void getNewKlass() {
        assertThat(klassService.getNewKlass()).isNotNull();
    }
{% endhighlight %}

## 实现类编码

{% highlight java %}
    @Override
    public Teacher getNewTeacher() {
        Teacher teacher = new Teacher(
                "示例教师",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                true);
        teacherRepository.save(teacher);
        return teacher;
    }
{% endhighlight %}

单元测试：

![{{site.imageurl}}/chapter3/25.png]({{site.imageurl}}/chapter3/25.png)

{% highlight java %}
    @Override
    public Klass getNewKlass() {
        Klass klass = new Klass();
        klass.setName("示例班级");                               // 设置名称
        klass.setTeacher(teacherService.getNewTeacher());       // 设置教师

        klassRepository.save(klass);
        return klass;
    }
{% endhighlight %}

单元测试：

![{{site.imageurl}}/chapter3/26.png]({{site.imageurl}}/chapter3/26.png)

有了这两个方法，当我们再次需要一个新的实体时，便可以直接调用上述方法来获取一个新的实体，不需要再次写冗余的新建实体代码了。


