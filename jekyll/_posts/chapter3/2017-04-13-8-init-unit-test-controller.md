---
layout: post
title:  "第八节：初始化单元测试 -- controller -- get"
description: "本节中，我们使用了`mockMvc`模拟进行`http`请求，并使用`print()`方法，将请求结果打印到了控制台中。最后，使用`jsonPath()`对响应结果进行了`json`转换，转换后，使用`is()`成功的进行了断言。"
date: 2017-04-14T13:49:00+08:00
categories: chapter3
author: "潘杰"
---
前面我们在开发`service`及`respository`时，都引用了单元测试。在开发`controller`时，采用的是`postMan`的方式。
使用`postMan`的优点的操作简单，数据返回直观。缺点当然也是有的，比如上节中，我们遇到的问题。那么，我们在开发`controler`时，是否也可以引用单元测试呢？

答案是肯定的。`Spring`的官方入门教程，对这个问题进行了讲解:
[https://spring.io/guides/gs/testing-web/](https://spring.io/guides/gs/testing-web/)

在官方教程的`Building REST services with Spring`中，也专门对控制器测试进行了讲解：[https://spring.io/guides/tutorials/bookmarks/](https://spring.io/guides/tutorials/bookmarks/)

此外，官方文档中，测试又单独的做为一个章节来呈现给我们：
[https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)



# 基本代码
我们结合`idea`生成基本的测试代码如下：
{% highlight java %}
package com.mengyunzhi.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by panjie on 17/4/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc       // 自动配置 模块MVC
public class KlassControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void save() throws Exception {
      
    }

    @Test
    // 由于get()与http请求的get命名相同，在这里重新命名以防止冲突。
    public void getTest() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }
}
{% endhighlight %}

## getTest
添加注释:
{% highlight java %}
    @Test
    // 由于get()与http请求的get命名相同，在这里重新命名以防止冲突。
    public void getTest() throws Exception {
        // 直接添加一个实体
        
        // 发起http请求来查询这个实体

        // 断言这个实体查询成功
    }
{% endhighlight %}

添加实体：
{% highlight java %}
    // 由于get()与http请求的get命名相同，在这里重新命名以防止冲突。
    public void getTest() throws Exception {
        // 直接添加一个实体
        Klass klass = klassService.getNewKlass();
        String klassId = klass.getId().toString();

        // 发起http请求来查询这个实体

        // 断言这个实体查询成功
    }
{% endhighlight %}

测试：
{% highlight text %}
Hibernate: insert into teacher (address, email, name, sex) values (?, ?, ?, ?)
Hibernate: insert into klass (name, teacher_id) values (?, ?)
{% endhighlight %}

<hr />
查询实体：
{% highlight java %}
    public void getTest() throws Exception {
        // 直接添加一个实体
        Klass klass = klassService.getNewKlass();
        String klassId = klass.getId().toString();

        // 发起http请求来查询这个实体
        // 1.发请请求
        // 2. 打印请求结果

        // 断言这个实体查询成功
    }
{% endhighlight %}

发起请求：
{% highlight java %}
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.ResultActions;
        // 1.发请请求
        ResultActions resultActions = this.mockMvc.perform(get("/klass/" + klassId));
{% endhighlight %}
解读：`ResultActions`返回类型，`perform`是`this.mockMvc`中的一个方法，接收的参数是`get()`方法的返回值，`get`方法的接收参数是绝对路径的字符串。

打印请求结果：
{% highlight java %}
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
        // 2. 打印请求结果
        resultActions.andDo(print());
{% endhighlight %}
代码解读：`andDo`是`resultActions`对象中的方法，这个方法接收的类型是`print()`函数的返回值。这两项共同起到了讲请求结果打印至控制台的作用。


测试：
{% highlight java %}
MockHttpServletRequest:         // 模拟HTTP请求信息
      HTTP Method = GET         // 请求方法
      Request URI = /klass/1    // 请求地址
       Parameters = {}          // 请求参数
          Headers = {}          // 请求头信息
// 处理信息
Handler:    
            // 请求类型（对应的类名） 请求方法（对应的方法名） 请求方法中的参数类型
             Type = com.mengyunzhi.controller.KlassController
           Method = public com.mengyunzhi.repository.Klass  com.mengyunzhi.controller.KlassController.get(java.lang.Long)

Async:      // 异步信息
    Async started = false       // 是否异步请求
     Async result = null        // 异步结果 

Resolved Exception:             // 异常信息
             Type = null

ModelAndView:                   // ModelAndView 在我们前台后分离中，这个暂时用不到。
        View name = null
             View = null
            Model = null

FlashMap:
       Attributes = null

MockHttpServletResponse:        // 模拟请求响应
           Status = 200         // 状态码 200 
    Error message = null        // 错误信息
          Headers = {Content-Type=[application/json;charset=UTF-8]}    // Header信息 
     Content type = application/json;charset=UTF-8                      // Content type
             Body = {"id":2,"teacher":{"id":2,"name":"示例教师","email":"zhangsan@yunzhiclub.com","address":"scse of hebut","sex":true},"name":"示例班级"} // Body，返回的主要内容
    Forwarded URL = null
   Redirected URL = null
          Cookies = []
{% endhighlight %}
在控制台中，详细的打印请求及该请求对应的响应信息。是的，这部分内容我们再熟悉不过了。有了伟大`MockMVC`，我们再也不必为交流测试而发愁了。离优秀的团队开发，我们又近了一步。

打印请求结果

<hr />
{% highlight java %}
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
    public void getTest() throws Exception {
        // 直接添加一个实体
        Klass klass = klassService.getNewKlass();
        String klassId = klass.getId().toString();

        // 发起http请求来查询这个实体
        // 1.发请请求
        ResultActions resultActions = this.mockMvc.perform(get("/klass/" + klassId));
        // 2. 打印请求结果
        resultActions.andDo(print());

        // 断言这个实体查询成功
        resultActions.andExpect(jsonPath("$.id", is(klass.getId().intValue())));
    }
{% endhighlight %}
解读：`andExpect` -- 建立一个断言， `jsonPath()` -- 将返回的`body`数据转化为`json`并进行断言判断, `$.id` -- `$`代表返回的`json`对象，`is()` -- 断言值。
上述代码整体表示：建立一个断言：期待返回的结果成功转化为`json`格式的数据，转化为`json`格式数据后，其中的`id`属性的值为`klass`的`id`值。

测试：

![{{site.imageurl}}/chapter3/34.png]({{site.imageurl}}/chapter3/34.png)

## 代码重构
上述代码采用链式调用后，如下：
{% highlight java %}
    @Test
    // 由于get()与http请求的get命名相同，在这里重新命名以防止冲突。
    public void getTest() throws Exception {
        // 直接添加一个实体
        Klass klass = klassService.getNewKlass();

        // 发起http请求来查询这个实体
        this.mockMvc.perform(get("/klass/" + klass.getId().toString()))
            .andDo(print())
            .andExpect(jsonPath("$.id", is(klass.getId().intValue())));
    }
{% endhighlight %}

在教程的编写过程中，`IDEA`并没能自动为我们添加好所有的依赖包。故在此给出完事的代码供参考:
{% highlight java %}
package com.mengyunzhi.controller;

import com.mengyunzhi.repository.Klass;
import com.mengyunzhi.service.KlassService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;


/**
 * Created by panjie on 17/4/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc       // 自动配置 模似MVC
public class KlassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KlassService klassService;

    @Test
    public void save() throws Exception {

    }

    @Test
    // 由于get()与http请求的get命名相同，在这里重新命名以防止冲突。
    public void getTest() throws Exception {
        // 直接添加一个实体
        Klass klass = klassService.getNewKlass();

        // 发起http请求来查询这个实体
        this.mockMvc.perform(get("/klass/" + klass.getId().toString()))
                .andDo(print())
                .andExpect(jsonPath("$.id", is(klass.getId().intValue())));
    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }
}
{% endhighlight %}

# 总结：
本节中，我们使用了`mockMvc`模拟进行`http`请求，并使用`print()`方法，将请求结果打印到了控制台中。最后，使用`jsonPath()`对响应结果进行了`json`转换，转换后，使用`is()`成功的进行了断言。

## 参考

[https://spring.io/guides/gs/testing-web/](https://spring.io/guides/gs/testing-web/)|[https://spring.io/guides/tutorials/bookmarks/](https://spring.io/guides/tutorials/bookmarks/)|[https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html) | [https://docs.spring.io/spring-boot/docs/current/reference/html/test-auto-configuration.html](https://docs.spring.io/spring-boot/docs/current/reference/html/test-auto-configuration.html)
