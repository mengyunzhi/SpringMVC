---
layout: post
title:  "第二节：测试数据表"
description: "使用单元测试对数据表进行关联测试"
date: 2017-04-13T11:54:05+08:00
categories: chapter3
author: "潘杰"
---
## 测试
由于我们只是增加了一个接口，而接口的实现是由`SpringMVC`完成的，所以原则上，我们并不需要对其中的方法的正确与否进行测试。我们要做的仅仅是，调用这个接口进行某一个操作，是否可以得到我们的预期结果。如果某一个操作测试成功了，我们就认为整体的接口便是测试通过的。

{% highlight java %}
package com.mengyunzhi.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by panjie on 17/4/6.
 */
// 使用以下两个注解来说明：本测试类基于SpringBoot。(必须)
@RunWith(SpringRunner.class) @SpringBootTest
public class KlassRepositoryTest {
    // @Autowired注解：自动加载Spring为我们自动实例化的实现了KlassRepository接口的对象
    @Autowired
    private KlassRepository klassRepository;

    // @Before注解：在执行单元测试方法 前 执行。
    @Before
    public void before() {
    }

    // @After：在执行单元测试方法 后 执行。
    @After
    public void after() {
    }

    @Test
    public void test() {
        
    }
}
{% endhighlight %}

请思考：我们在此使用到了`KlassRepository`，但为什么不需要`import`呢？

### 班级中不含教师
{% highlight java %}
    @Test
    public void test() {
        Klass klass = new Klass();
        klass.setName("网络一班");
        assertThat(klassRepository.save(klass)).isNotNull();
    }
{% endhighlight %}
**[注意]** `assertThat`包可能需要手动引用。

测试结果：
![{{site.imageurl}}/chapter3/9.png]({{site.imageurl}}/chapter3/9.png)

![{{site.imageurl}}/chapter3/10.png]({{site.imageurl}}/chapter3/10.png)

### 乱码
数据表中，出现了乱码，这是由于我们虽然在创建数据库时，指定了数据库的默认编码为`utf-8`，但却把这项信息告知`SpringMVC`,为此，我们在配置文件中，将字符编码类型写入。
{% highlight xml %}
# 指定连接的类型为mysql 连接的地址为：localhost 端口为3306 ，数据为springmvc, 编码类型utf-8
spring.datasource.url=jdbc:mysql://localhost:3306/springmvc?characterEncoding=UTF-8
{% endhighlight %}

配置后，当我们再次进行测试时，发现乱码的问题已经被成功解决了。

### 班级中存在教师
{% highlight java %}
    @Test
    public void addDataWithTeacher() {
        Teacher teacher = new Teacher("zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                false);
        Klass klass = new Klass(teacher, "一一班");
        klassRepository.save(klass);
    }
{% endhighlight %}

测试，我们将得到以下异常。
{% highlight console %}
org.springframework.dao.InvalidDataAccessApiUsageException: org.hibernate.TransientPropertyValueException: 
    object references an unsaved transient instance - save the transient instance before flushing : 
        com.mengyunzhi.repository.Klass.teacher -> com.mengyunzhi.repository.Teacher; nested exception is java.lang.IllegalStateException:
            org.hibernate.TransientPropertyValueException: 
                object references an unsaved transient instance - save the transient instance before flushing : 
                    com.mengyunzhi.repository.Klass.teacher -> com.mengyunzhi.repository.Teacher
{% endhighlight %}

我们将后数的第2句进行翻译后,大概是说：对象引用了一个没有被保存的临时实例 -(解决方法) 在进行数据持久化（这里指`save`操作）这前，将这个临时实例进行保存。

我们大概分析一下，也是这样。我们虽然新例化了一个`teacher`, 但我们进行`klass`的数据存储时，需要用到`teacher`里的`id`字段，然后再进行存储。我们暂时把这个流程想像成。

![{{site.imageurl}}/chapter3/11.png]({{site.imageurl}}/chapter3/11.png)

按上述流程图，当执行到第3步，获取教师实体时，发现返回值为`null`，随后抛出了上述异常。实际上是这样吗？我们再测试：

{% highlight java %}
    @Test
    public void addDataWithTeacher() {
        Teacher teacher = new Teacher("zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                false);
        teacher.setId(1000L);
        Klass klass = new Klass(teacher, "一一班");
        klassRepository.save(klass);
    }
{% endhighlight %}

此时将得到如下提示： 
{% highlight console %}
2017-04-13 11:41:43.029  WARN 9005 --- [           main] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 1452, SQLState: 23000
2017-04-13 11:41:43.030 ERROR 9005 --- [           main] o.h.engine.jdbc.spi.SqlExceptionHelper   : Cannot add or update a child row: a foreign key constraint fails (`springmvc`.`klass`, CONSTRAINT `FK2deq3vjpqt9i2282hhgrhc708` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`))

org.springframework.dao.DataIntegrityViolationException: could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement
{% endhighlight %}

是样代码的提示告诉我们，事实上`SpringMVC`不仅仅是获取了教师`id`的值，还会在进行数据插入时，去较验这个外键值对应的数据是否存在，如果存在，则会报错。

我们看异常的类型 -- `SqlExceptionHelper`，大概可以猜出其是由`SqlServer`抛出的。对于现阶段的我们，完全不必理会是`SpringMVC`还是`Hibernate`或是`MySql`抛出的这个异常，我们只需要知道，在进行数据插入以前，如果加入了关联的实体，那么，这个实体在数据表中必须是真实存在的，否则将会抛出一个异常。

为了说明，本测试就是要抛出异常的，我们对其方法进行如下注解：
{% highlight java %}
    @Test(expected = DataIntegrityViolationException.class)
    public void addDataWithTeacher() {
        Teacher teacher = new Teacher("zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                false);
        teacher.setId(1000L);
        Klass klass = new Klass(teacher, "一一班");
        klassRepository.save(klass);
    }
{% endhighlight %}

>请思索：异常类型为什么是`DataIntegrityViolationException`？

最后，我们再增加一个无异常的测试。
{% highlight java %}
    @Autowired
    private TeacherRepository teacherRepository;
    @Test
    public void addKlassWithExistTeacher() {
        Teacher teacher = new Teacher("zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                false);
        teacherRepository.save(teacher);
        Klass klass = new Klass(teacher, "一一班");
        assertThat(klassRepository.save(klass)).isNotNull();
    }
{% endhighlight %}

测试测试类：
![{{site.imageurl}}/chapter3/12.png]({{site.imageurl}}/chapter3/12.png)

# 关联删除
在设定外键约束后，不仅在数据添加时会进行验证，在执行相关的删除操作时，同样会进行相关较验。在上面的测试类执行后，不出意外的，你将在数据表中得到两个数据表，三条数据。我们测试下在未删除班级表中数据的前提下，直接删除教师数据，看会报什么异常。

![{{site.imageurl}}/chapter3/1.gif]({{site.imageurl}}/chapter3/1.gif)

正如我们期待的一样，`Navicat`在执行数据删除时，发生了`foreign key constraint fails(外键约束失败)`异常 -- `Cannot delete or update a parent row: a foreign key constraint fails (`springmvc`.`klass`, CONSTRAINT `FK2deq3vjpqt9i2282hhgrhc708` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`))`。

我们使用单元测试的方法，同样能测试出关联删除异常的错误。

{% highlight java %}
    @Test
    public void relationDeleteException() {
        Teacher teacher = new Teacher("zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                false);
        teacherRepository.save(teacher);
        Klass klass = new Klass(teacher, "一一班");
        klassRepository.save(klass);
        teacherRepository.delete(teacher);
    }
}
{% endhighlight %}

控制台：
{% highlight console %}
Hibernate: delete from teacher where id=?
2017-04-13 13:45:37.662  WARN 11316 --- [           main] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 1451, SQLState: 23000
2017-04-13 13:45:37.662 ERROR 11316 --- [           main] o.h.engine.jdbc.spi.SqlExceptionHelper   : Cannot delete or update a parent row: a foreign key constraint fails (`springmvc`.`klass`, CONSTRAINT `FK2deq3vjpqt9i2282hhgrhc708` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`))
2017-04-13 13:45:37.664  INFO 11316 --- [           main] o.h.e.j.b.internal.AbstractBatchImpl     : HHH000010: On release of batch it still contained JDBC statements

org.springframework.dao.DataIntegrityViolationException: could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement
{% endhighlight %}

我们看到，控制台不仅打印出了`MySql`的报错信息，还抛出了一个类型为`org.springframework.dao.DataIntegrityViolationException`的异常。

加入异常断言，完善测试代码：
{% highlight java %}
    @Test(expected = DataIntegrityViolationException.class)
    public void relationDeleteException() {
        Teacher teacher = new Teacher("zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                false);
        teacherRepository.save(teacher);
        Klass klass = new Klass(teacher, "一一班");
        klassRepository.save(klass);
        teacherRepository.delete(teacher);
    }
{% endhighlight %}
最终执行单元测试类测试如下：

![{{site.imageurl}}/chapter3/13.png]({{site.imageurl}}/chapter3/13.png)

请思索：单元测试前，数据表中没有任何数据；我们经过单元测试后，观察数据表发现在数据表中产生了4条数据。这可能会导致什么问题？如何规避这种问题的产生。

> 参考：| [https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.htm](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.htm)
