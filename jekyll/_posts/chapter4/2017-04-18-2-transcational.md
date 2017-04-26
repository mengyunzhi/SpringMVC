---
layout: post
title:  "第二节：深入理解事务"
description: "通过单元测试深入理解事务"
date: 2017-04-26T11:05:41+08:00
categories: chapter4
author: "潘杰"
---
上一节中，我们在测试的方法中，加入了`@Transactional`注解。这使得`spring`在此方法结束前，不去关闭事务，从而达到了正确处理数据的目的。其实除了将`@Transactional`加入到方法上以外，还可以将其加入到类的注解中。比如：

{% highlight java %}
@RunWith(SpringRunner.class) @SpringBootTest @Transactional
public class CourseRepositoryTest {
{% endhighlight %}

这样一来，我们就不需要为每个方法单独加入应用事务的注解了。那么我们说什么是事务呢？简单来讲，事务就是将一组数据库操作语句当做一条来进行执行，如果执行成功，那么就全部执行成功，如果执行失败呢，那么就全部执行失败。下面，我们使用`idea`另一个强大的功能 -- 中断，来具体理解 事务 这个即陌生又熟悉的名词。

# JPA
我们早早的就接触了 `jpa`这个名词，那么具体在使用过程中，哪里体现了`JPA`呢？为了更好的理解这个问题，我们去掉`@Transactional`注解，然后为了防止惰性加载的异常，注释到最后需要事务才能返回数据的代码。

{% highlight java %}
@RunWith(SpringRunner.class) @SpringBootTest
public class CourseRepositoryTest {
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
//        assertThat(course.getKlass().size()).isEqualTo(2);
    }
{% endhighlight %}

再然后，我们在方法的第一行，打一个断点，并在启动单元测试时选择`debug`。

### debug

![{{site.imageurl}}/chapter4/4.png]({{site.imageurl}}/chapter4/4.png)

此时，代码被`IEDA`中断执行了，变量的类型与值，直接显示在了`idea`的编辑界面中，这的确是我们使用过的最人性化的编辑器了。

![{{site.imageurl}}/chapter4/5.png]({{site.imageurl}}/chapter4/5.png)

和其它的调试系统一样，这里我们可以使用步进、步入、步出来进行程序的调试。除此以外，`idea`还提供了排除库文件的步入与步出。在前期，我们只需要使用两点： 步进，即执行下一行；跳到下一断点。

![{{site.imageurl}}/chapter4/6.png]({{site.imageurl}}/chapter4/6.png)

### 数据持久化
我们在第一行打断点的情况，启动`debug`。

![{{site.imageurl}}/chapter4/5.png]({{site.imageurl}}/chapter4/5.png)

然后查看数据表中，发现teacher表中和klass表中，并没有生成数据。此时，我们将点"步进"，执行到下一行。

再查看数据表：

![{{site.imageurl}}/chapter4/7.png]({{site.imageurl}}/chapter4/7.png)

![{{site.imageurl}}/chapter4/8.png]({{site.imageurl}}/chapter4/8.png)

这说明，执行完第1行代码后，`spring`执行了`sql`语句，完成了将实体变为数据表中的对应数据的过程。我们说把数据写入到了数据库，这个数据即使是程序关闭，也仍然会存在，那么就认为是持久存在了。所以，这个过程就是数据持久化。

此时，我们在 步进，再打开数据表查看，还会发现又生成了两条新数据。也就是说，第2行，`spring`也进行数据库的具体操作，从而将数据写入到了数据库。

看完这个过程以后，我们在加入事务注解，重新看看具体的执行过程中会发生什么。

### 加入事务

我们在第一行打断点的情况，启动`debug`。

![{{site.imageurl}}/chapter4/5.png]({{site.imageurl}}/chapter4/5.png)

和前面一样，我们每 步进 一步，便查看一便数据表。没错，奇怪的事情发生了，数据表中，竟然。。。没有生成一条数据。是的，正如你看到的这样，由于将这个方法使用事务进行了注解，所以在方法执行完毕前，事务中的`sql`语句是不会进行提交的。那么，由于没有进行`sql`语句的提交，当然也就不会在数据表中生成数据了。

那么，为什么当这个方法执行完毕后，我们去查看数据表，仍然看不到任何数据呢？按上面的说法，当方法结束后，事务应该进行了提交，那么数据表当中应该生成数据才对。

这是由于我们当前使用的是`junit`单元测试，当它看到我们将方法使用 事务 进行注解时，单元测试的方法完毕后，`junit`直接指挥下的事务，并没有进行任何的`sql`语句提交的工作。所以，我们当然在数据表中看不到数据了。当然了，这正是我们在单元测试中还要的结果，进行完单元测试时，我们对数据表没有进行任何的更改，但却起到了测试的效果。

最后，为了验证 事务 的确是在方法结束后起作用的。我们新建`CourseSerive`方法，并在其方法上应用`@Transactional`注解，最后进行测试。

# service层应用事务

## 新建接口

{% highlight java %}
package com.mengyunzhi.service;

/**
 * Created by panjie on 17/4/26.
 */
public interface CourseService {
    void transactional();
}

{% endhighlight %}

## 实现接口

{% highlight java %}
package com.mengyunzhi.service;

import com.mengyunzhi.repository.Course;
import com.mengyunzhi.repository.CourseRepository;
import com.mengyunzhi.repository.Klass;
import com.mengyunzhi.repository.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * Created by panjie on 17/4/26.
 */
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private KlassService klassService;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    @Transactional
    public void transactional() {
        // 创建两个班级
        Klass klass1 = klassService.getNewKlass();
        Klass klass2 = klassService.getNewKlass();

        Teacher teacher = new Teacher();

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
        return;
    }
}

{% endhighlight %}

## 测试接口

{% highlight java %}
package com.mengyunzhi.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by panjie on 17/4/26.
 */
@RunWith(SpringRunner.class) @SpringBootTest
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;

    @Test
    public void transactional() {
        courseService.transactional();
        return;
    }
}
{% endhighlight %}

## 打中断，测试

我们如下图，打3个断点。

![{{site.imageurl}}/chapter4/9.png]({{site.imageurl}}/chapter4/9.png)

![{{site.imageurl}}/chapter4/10.png]({{site.imageurl}}/chapter4/10.png)

然后启动`debug`，并按点击 跳转到下一断点 来直接查看执行到此的结果。

![{{site.imageurl}}/chapter4/11.png]({{site.imageurl}}/chapter4/11.png)

最终结果和我们预测的一致，当执行完带有`@Transactional`注解的`transactional()`方法时，数据被持久化到了数据表。

试想下，如果在执行带有`@Transactional`注解的方法时，发生异常，那么数据就不会被久持化，这就是我们平常所说的“回滚”。也就是说，`spring`的事务管理中的 回滚 是针对每一个事务而言的，在一个事务中，如果发生了错误或是异常，那么这个事务就不进行提交，从而达到了类似于 回滚 的效果。每这个事务已经结束，那么数据是没有办法进行 回滚 的，因为此时数据已经被持久化到了数据表中。在实际的项目中，我们将会更深入的接触到这个问题。相信到时候你会更加清晰的掌握事务这个东西。


