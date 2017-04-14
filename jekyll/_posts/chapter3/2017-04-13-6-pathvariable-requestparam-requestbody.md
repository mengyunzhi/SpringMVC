---
layout: post
title:  "第六节：@PathVariable @RequestRaram @RequestBody"
description: "讲述并验证了3种常用的获取输入变量的注解"
date: 2017-04-14T13:47:21+08:00
categories: chapter3
author: "潘杰"
---
在一章中，我们使用了`@PathVariable`获取了`pathInfo`中的变量，使用`@RequestBody`获取到了`post`过来`json`数据。除了上述两种方法外，来有一种获取输入变量的注解`@RequestRaram`。

本节中，让我们结合班级管理，同时通过三个测试代码段，来更深一步的对其进行学习。

首先，我们参考类图及时序图新建控制器。

![{{site.imageurl}}/chapter3/18.png]({{site.imageurl}}/chapter3/18.png)

# 新建控制器
新建控制器，并引用日志类`org.slf4j.Logger`
{% highlight java %}
package com.mengyunzhi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by panjie on 17/4/13.
 */
@RestController
@RequestMapping("/klass")
public class KlassController {
    // 引入日志类的固有写法
    private final static Logger logger = LoggerFactory.getLogger(KlassController.class);
}

{% endhighlight %}

然后，我们分别进行测试：
### @PathVariable

{% highlight java %}
    @RequestMapping("/test/{value}")
    public void test(
            @PathVariable String value,) {
        logger.info("获取到的pathVariable为:" + value);
    }
{% endhighlight %}

测试：

![{{site.imageurl}}/chapter3/3.gif]({{site.imageurl}}/chapter3/3.gif)

### @RequestParam
{% highlight java %}
    @RequestMapping("/test/{value}")
    public void test(
            @PathVariable String value,
            @RequestParam String param) {
        logger.info("获取到的pathVariable为:" + value);
        logger.info("获取到的requestParam为:" + param);

    }
{% endhighlight %}

测试：

![{{site.imageurl}}/chapter3/4.gif]({{site.imageurl}}/chapter3/4.gif)

### @RequestBody

相对前面两个注解，@Requestbody比较灵活。

#### 1.直接转化为字符串
{% highlight java %}
    @RequestMapping("/test/{value}")
    public void test(
            @PathVariable String value,
            @RequestParam String param,
            @RequestBody(required = false) String requestBody) {
        logger.info("获取到的pathVariable为:" + value);
        logger.info("获取到的requestParam为:" + param);
        logger.info("获取到的RequestBody为:" + requestBody);
    }
{% endhighlight %}

> `required = false`表示，此项非必须。
分别点一下这些标签卡，然后随便加一些信息，最后点击`code`查看`postman`为我们自动生成的数据提交代码，看看最终我们在控制台都打印出了什么内容。

#### 2.转化为MultiValueMap

下面，为了更清晰的查看的@RequestBody绑定行为，我们去掉获取`@PathVariable`及`@RequestParam`的代码，并我们对代码进行如下改写：
{% highlight java %}
    @RequestMapping("/test/")
    public void test(@RequestBody MultiValueMap<String, String> requestBody) {
        logger.info("获取到的RequestBody为:" + requestBody.toString());
    }
{% endhighlight %}

自己测试一下吧，看看在控制台中，都打印了什么信息。

#### 3.绑定（转化为）到对象

{% highlight java %}
package com.mengyunzhi.controller;

import com.mengyunzhi.service.KlassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import com.mengyunzhi.repository.Klass;

/**
 * Created by panjie on 17/4/13.
 */
@RestController
@RequestMapping("/klass")
public class KlassController {
    private final static Logger logger = LoggerFactory.getLogger(KlassController.class);

    @Autowired
    private KlassService klassService;

    @RequestMapping("/test/")
    public void test(@RequestBody JsonInput jsonInput) {
        logger.info("获取到的RequestBody为:" + jsonInput.toString());
    }

    private static class JsonInput {
        private String name;
        private String sex;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        @Override
        public String toString() {
            return "JsonInput{" +
                    "name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    '}';
        }

        public JsonInput() {
        }

        public JsonInput(String name, String sex) {
            this.name = name;
            this.sex = sex;
        }
    }
}

{% endhighlight %}

![{{site.imageurl}}/chapter3/28.png]({{site.imageurl}}/chapter3/28.png)

上面的代码中，我们建立了一个私有对象，并使用`@RequestBody`将传入的`Json`数据绑定到了这个私有对象上,最终成功的在控制台将其打印了出来。

## 总结：

@PathVariable | 接收pathInfo中的变量 
@RequestParam | 接收get和post信息,当两者都存在同一变时，进行字符串拼接。
@RequestBody  | 形式多样，但做API，我们主要接收`Content-Type`为`application/json`的`json`信息。

<hr />
参考文档:

@PathVariable | [官方文档](https://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html#mvc-ann-requestmapping-uri-templates)
@RequestParam | [官方文档](https://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html#mvc-ann-requestparam)
@RequestBody  | [官方文档](https://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html#mvc-ann-requestbody)
MultiValueMap | [http://blog.csdn.net/yanzhenjie1003/article/details/51550264](http://blog.csdn.net/yanzhenjie1003/article/details/51550264)


最终测试代码：
{% highlight java %}
package com.mengyunzhi.controller;

import com.mengyunzhi.service.KlassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


/**
 * Created by panjie on 17/4/13.
 */
@RestController
@RequestMapping("/klass")
public class KlassController {
    private final static Logger logger = LoggerFactory.getLogger(KlassController.class);

    @Autowired
    private KlassService klassService;

    @RequestMapping("/test/")
    public void test(@RequestBody JsonInput jsonInput) {
        logger.info("获取到的RequestBody为:" + jsonInput.toString());
    }

    private static class JsonInput {

        private String name;
        private String sex;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        @Override
        public String toString() {
            return "JsonInput{" +
                    "name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    '}';
        }

        public JsonInput() {
        }

        public JsonInput(String name, String sex) {
            this.name = name;
            this.sex = sex;
        }
    }
}
{% endhighlight %}