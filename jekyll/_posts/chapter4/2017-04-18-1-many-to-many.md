---
layout: post
title:  "第一节：多对多"
description: "描述信息"
date: 2017-04-18T14:09:56+08:00
categories: chapter4
author: "潘杰"
---
在前面的章节中，我们使用了多对一关联。本节中，我们增加课程管理来查看如何进行多对多的关联：

编码先画图，建立数据表当然也是如此。

![{{site.imageurl}}/chapter4/1.png]({{site.imageurl}}/chapter4/1.png)

如上图所示，一个班级对应多个课程，一个课程也对应多个班级，两个实体是一对一的关系。

# 定义实体

{% highlight java %}
package com.mengyunzhi.repository;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by panjie on 17/4/25.
 */
@Entity
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;    // 名称

    @ManyToMany
    private Set<Klass> klass = new HashSet<Klass>();
}

{% endhighlight %}

我们使用了`@ManyToMany`注解，来说明了实体间的关系为多对多。那么如此声明以后`SpringMVC`能为我们自动做什么呢？

> 变量名设置为: `klass` -- 它直接对应到中间表名、中间表字段名。

下面，我们让我们来添加`set\get`及其它辅助函数，最终来进行实际验证。

{% highlight java %}
package com.mengyunzhi.repository;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by panjie on 17/4/25.
 */
@Entity
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;    // 名称

    @ManyToMany
    private Set<Klass> klass = new HashSet<Klass>();

    public Course(String name, HashSet<Klass> klass) {
        this.name = name;
        this.klass = klass;
    }

    public Course() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Klass> getKlass() {
        return klass;
    }

    public void setKlass(Set<Klass> klass) {
        this.klass = klass;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", klass=" + klass +
                '}';
    }
}
{% endhighlight %}

# 新建repository

{% highlight java %}
package com.mengyunzhi.repository;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by panjie on 17/4/25.
 */
public interface CourseRepository extends CrudRepository<Course, Long> {
}

{% endhighlight %}

# 测试

{% highlight java %}
package com.mengyunzhi.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;
/**
 * Created by panjie on 17/4/25.
 */
@RunWith(SpringRunner.class) @SpringBootTest
public class CourseRepositoryTest {
    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void test() {
        // 无异常，则说明测试通过
        courseRepository.findOne(1L);
    }
}
{% endhighlight %}

执行测试用例，我们将得到如下数据表：

![{{site.imageurl}}/chapter4/2.png]({{site.imageurl}}/chapter4/2.png)

同时，为我们设置了索引及外键：

![{{site.imageurl}}/chapter4/3.png]({{site.imageurl}}/chapter4/3.png)

# 测试多对多

写注释:
{% highlight java %}
    @Test
    public void manyToMany() {
        // 创建两个班级
        // 创建一个课程
        // 设置这个课程对应刚刚创建的两个班级

        // 保存数据

        // 查询并断言
        // 断言能查到这个实体
        // 断言这个实体上对应两个班级实体
    }
{% endhighlight %}

添加代码:
{% highlight java %}
    @Test
    public void manyToMany() {
        // 创建两个班级
        Klass klass1 = klassService.getNewKlass();
        Klass klass2 = klassService.getNewKlass();

        // 创建一个课程
        Course course = new Course();
        course.setName("测试课程");

        // 设置这个课程对应刚刚创建的两个班级
        HashSet<Klass> klasses = new HashSet<Klass>();
        klasses.add(klass1);
        klasses.add(klass2);
        course.setKlass(klasses);

        // 保存数据
        courseRepository.save(course);

        // 查询并断言
        course = courseRepository.findOne(course.getId());
        // 断言能查到这个实体
        assertThat(course).isNotNull();
        // 断言这个实体上对应两个班级实体
    }
{% endhighlight %}

运行单元测试后，我们将在关联数据表中查询到我们刚刚添加的数据，测试用例通过。

最后，我们再添加另一个断言。看看查询出这个课程对应两个班级。
{% highlight java %}
        ...
        // 查询并断言
        course = courseRepository.findOne(course.getId());
        // 断言能查到这个实体
        assertThat(course).isNotNull();
        // 断言这个实体上对应两个班级实体
        assertThat(course.getKlass().size()).isEqualTo(2);
    }
{% endhighlight %}

运行单元测试，我们将得到一个如下错误：
{% highlight console %}
org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.mengyunzhi.repository.Course.klass, could not initialize proxy - no Session
{% endhighlight %}

按关键字去搜索，我们发现原来错误产生的原因有两点：

1. 由于`hibernate`的延迟（惰性）加载，在查询到课程实体时，没有马上取出其对应的班级实体信息。
2. 当我们想取其信息时，此时已经没有可用于数据库操作的`session`。这是由于`spring`对`session`进行了统一管理，为了防止误操作带来的错误，`spring`在进行完数据库操作后，自动的关闭了`session`。

在搜索的过程中，我们也会看到多种解决方案。经过多种解决方案对比，最终我们采用添加`@Transactional`的方式来解决这个问题。`@Transactional`表示：当本方法执行完毕后，再关闭`session`。

{% highlight java %}
    @Test
    @Transactional
    public void manyToMany() {
{% endhighlight %}

此时，单元测试顺利通过了。

> 注：在不需要对中间表进行除CRUD以外的操作时，我们使用多对多的注解来进行实现。当需要进行其它操作时，我们需要使用多对一的关联进行实现。

> 参考: | [https://docs.jboss.org/hibernate/stable/annotations/reference/en/html_single/#entity-mapping-association](https://docs.jboss.org/hibernate/stable/annotations/reference/en/html_single/#entity-mapping-association) | 


