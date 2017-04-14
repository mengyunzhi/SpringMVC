---
layout: post
title:  "第八节：初始化单元测试 -- controller"
description: "初始化C层中对应的单元测试，告别使用postMan进行数据查看"
date: 2017-04-14T13:49:00+08:00
categories: chapter3
author: "潘杰"
---
前面我们在开发`service`及`respository`时，都引用了单元测试。在开发`controller`时，采用的是`postMan`的方式。
使用`postMan`的优点的操作简单，数据返回直观。缺点当然也是有的，比如上节中，我们遇到的问题。那么，我们在开发`controler`时，是否也可以引用单元测试呢？

答案是肯定的。`Spring`的官方入门教程，专门对这个问题进行了讲解。[https://spring.io/guides/gs/testing-web/](https://spring.io/guides/gs/testing-web/)

# 基本代码
我们结合`idea`生成基本的代码，并引入`assertThat`断言类以及设置`@Autowired`注解。
{% highlight java %}
package com.mengyunzhi.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by panjie on 17/4/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KlassControllerTest {
    @Autowired
    private KlassController controller;

    @Test
    public void save() throws Exception {

    }

    @Test
    public void get() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }

}
{% endhighlight %}

## save

