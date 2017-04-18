---
layout: post
title:  "第九节：单元测试 -- controller -- save"
description: "本节中，我们使用`mockMvc`进行模似`post`请求，并使用`jsonPath(\"$.id\", notNullValue())`来对返回值是否为`null`进行断言；触发异常，并使用`status().is4xxClientError()`对发生的异常成功进行了断言。"
date: 2017-04-17T14:53:57+08:00
categories: chapter3
author: "潘杰"
---
我们在上节中学习对如何测试`get`请求。`post`的请求与`get`请求基本相同。
我们使用`postMan`来查看下，`get`与`post`生成的实际数据都有什么。

get:
![{{site.imageurl}}/chapter3/36.png]({{site.imageurl}}/chapter3/36.png)

post:
![{{site.imageurl}}/chapter3/35.png]({{site.imageurl}}/chapter3/35.png)

通过对上面两张图片的对比，不难发现，`post`与`get`除了请求方式不同外，还需要指定了一个`content type`类型，另外，还需要加入发送的`json`数据。

本节中，让我们共同学习`SpringMVC`是怎么解决这两点内容的。

## content type
{% highlight java %}
    @Test
    public void save() throws Exception {
        // 提交POST请求,并设置contentType
        this.mockMvc
                .perform(post("/klass/")
                        .contentType("application/json"))
                .andDo(print());
    }
{% endhighlight %}

测试：
{% highlight text %}
MockHttpServletRequest:
      HTTP Method = POST
      Request URI = /klass/
       Parameters = {}
          Headers = {Content-Type=[application/json;charset=UTF-8]}
{% endhighlight %}

正如我们看到的一样，在调用`contentType()`，`mockMvc`在提交请示时，为我们设置了`Content-Type`的值，同时，还添加了系统的默认编码。

## json 数据
在使用`postMan`的过程中，我们可以尝试点击`body`标签中的各个选项卡，并输入一些测试数据，最后查看`code`。你会发现，各个不同类型反映至最终发送的数据上，一是`content type`值不同，二就是发送数据的格式不同。但本质上，都是在发送普通字符串。

![{{site.imageurl}}/chapter3/37.png]({{site.imageurl}}/chapter3/37.png)

json数据也是特定格式的定符串。
比如我们增加班级信息时发送的数据为：`{"name":"hello","teacherId":1}`。`mockMvc`为我们提供了`content()`函数，用于发送主体数据。
{% highlight java %}
    public void save() throws Exception {
        // 提交POST请求,并设置contentType
        this.mockMvc
                .perform(post("/klass/")
                        .contentType("application/json")
                .content("{\"name\":\"hello\",\"teacherId\":1}"))
                .andDo(print());
    }
{% endhighlight %}

`\"`用于转义`"`，转义后，`"`将做为一个普通的字符串来处理。

测试：
{% highlight text %}
MockHttpServletRequest:
      HTTP Method = POST
      Request URI = /klass/
       Parameters = {}
          Headers = {Content-Type=[application/json;charset=UTF-8]}

Handler:
             Type = com.mengyunzhi.controller.KlassController
           Method = public com.mengyunzhi.repository.Klass com.mengyunzhi.controller.KlassController.save(com.mengyunzhi.controller.KlassController$JsonInput)

Async:
    Async started = false
     Async result = null

Resolved Exception:
             Type = null

ModelAndView:
        View name = null
             View = null
            Model = null

FlashMap:
       Attributes = null

MockHttpServletResponse:
           Status = 200
    Error message = null
          Headers = {Content-Type=[application/json;charset=UTF-8]}
     Content type = application/json;charset=UTF-8
             Body = {"id":1,"teacher":null,"name":"hello"}
    Forwarded URL = null
   Redirected URL = null
          Cookies = []
{% endhighlight %}

## 完善测试代码
基本的用法学习后，我们完善测试代码。

注释：
{% highlight java %}
    public void save() throws Exception {
        // 获取一个有效的教师

        // 提交JSON POST请求, 断言收到json数据，并存在id值
    }
{% endhighlight %}

添加代码：
{% highlight java %}
import static org.hamcrest.Matchers.notNullValue;
    public void save() throws Exception {
        // 获取一个有效的教师
        Klass klass = klassService.getNewKlass();
        Long teacherId = klass.getTeacher().getId();

        // 提交JSON POST请求, 断言收到json数据，并存在id值
        this.mockMvc
                .perform(post("/klass/")
                        .contentType("application/json")
                        .content("{\"name\":\"hello\",\"teacherId\":" + teacherId.toString() + "}"))
                .andDo(print())
                .andExpect(jsonPath("$.id", notNullValue()));
    }
{% endhighlight %}

测试：

![{{site.imageurl}}/chapter3/38.png]({{site.imageurl}}/chapter3/38.png)

# 异常测试
在进行单元测试前，我们先回忆下使用`postMan`进行测试时可能发生的异常：

`name`为空发生的数据绑定校验异常:

![{{site.imageurl}}/chapter3/39.png]({{site.imageurl}}/chapter3/39.png)

`name`过长发生的数据绑定校验异常：

![{{site.imageurl}}/chapter3/40.png]({{site.imageurl}}/chapter3/40.png)

缺少必要参数时发生的数据绑定校验异常:

![{{site.imageurl}}/chapter3/41.png]({{site.imageurl}}/chapter3/41.png)

传入字符串非json格式时，发生的`json`数据读取异常:

![{{site.imageurl}}/chapter3/42.png]({{site.imageurl}}/chapter3/42.png)

![{{site.imageurl}}/chapter3/43.png]({{site.imageurl}}/chapter3/43.png)

![{{site.imageurl}}/chapter3/44.png]({{site.imageurl}}/chapter3/44.png)

我们并不关心异常的具体类型，我们关心的是：当输入错误的信息时是否发生异常。通过观察不然发现，上述各个异常类型虽然不同，但返回的状态码均为`400`。没错，我们就是利用这个特点，来检测是否发生了用户请求的异常。

## 测试异常

班级名称为空:
{% highlight java %}
    @Test
    public void saveWithNameEmpty() throws Exception {
        // 直接添加一个实体
        Klass klass = klassService.getNewKlass();
        Long teacherId = klass.getTeacher().getId();
        // 检测异常(用户名过短）
        this.mockMvc
                .perform(post("/klass/")
                        .contentType("application/json")
                        .content("{\"name\":\"\",\"teacherId\":" + teacherId.toString() + "}"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
{% endhighlight %}
代码分析：使用`status().`代表返回状态，`is4xxClientError()`代表断言是4xx的错误，比如400,401,402都属于4xx。

班级名称过长：
{% highlight java %}
    @Test
    public void saveWithNameTooLong() throws Exception {
        // 直接添加一个实体
        Klass klass = klassService.getNewKlass();
        Long teacherId = klass.getTeacher().getId();

        // 检测异常(用户名过长)
        this.mockMvc
                .perform(post("/klass/")
                        .contentType("application/json")
                        .content("{\"name\":\"sdfsdfsdfsdfsdf\",\"teacherId\":" + teacherId.toString() + "}"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
{% endhighlight %}

除使用`is4xxClientError()`来判断是否发生4xx错误以外，我们还可以使得`is(int status)`,如`is(400)`来判断发生的具体的状态码。还可以使用`isNotFound()`来判断是否发生`404`错误。`Spring`的官方`API`中，为我们总结了可用的断言方法:[https://docs.spring.io/spring/docs/4.3.7.RELEASE/javadoc-api/org/springframework/test/web/servlet/result/StatusResultMatchers.html](https://docs.spring.io/spring/docs/4.3.7.RELEASE/javadoc-api/org/springframework/test/web/servlet/result/StatusResultMatchers.html)。

> 至此，我们已经接触到了官方的两个最重要的文档：[官方参考文档](https://docs.spring.io/spring/docs/4.3.7.RELEASE/spring-framework-reference/htmlsingle/)，[官方API文档](https://docs.spring.io/spring/docs/4.3.7.RELEASE/javadoc-api/)。


## 代码重构
当我们开始重复造轮子的时候，就应该思索自己是否应该马上进行代码重构了。

{% highlight java %}
    private Klass klass;
    private Long teacherId;

    @Before
    public void before() {
        this.klass = klassService.getNewKlass();
        this.teacherId = klass.getTeacher().getId();
    }

    @Test
    public void save() throws Exception {
        String content = "{\"name\":\"hello\",\"teacherId\":" + teacherId.toString() + "}";

        // 提交JSON POST请求, 断言收到json数据，并存在id值
        this._save(content).andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    // 检测异常(用户名过短）
    public void saveWithNameEmpty() throws Exception {
        String content = "{\"name\":\"\",\"teacherId\":" + teacherId.toString() + "}";

        this._save(content).andExpect(status().is4xxClientError());
    }


    @Test
    // 检测异常(用户名过长)
    public void saveWithNameTooLong() throws Exception {
        String content = "{\"name\":\"sdfsdfsdfsdfsdf\",\"teacherId\":" + teacherId.toString() + "}";

        this._save(content).andExpect(status().is4xxClientError());
    }

    // 新增数据
    private ResultActions _save(String content) throws Exception {
        return this.mockMvc.perform(post("/klass/")
                .contentType("application/json")
                .content(content))
                .andDo(print());
    }
{% endhighlight %}

# 总结
本节中，我们使用`mockMvc`进行模似`post`请求，并使用`jsonPath("$.id", notNullValue())`来对返回值是否为`null`进行断言；触发异常，并使用`status().is4xxClientError()`对发生的异常成功进行了断言。


参考： | [StatusResultMatchers](https://docs.spring.io/spring/docs/4.3.7.RELEASE/javadoc-api/org/springframework/test/web/servlet/result/StatusResultMatchers.html) | [MockMvc](https://docs.spring.io/spring/docs/4.3.7.RELEASE/javadoc-api/org/springframework/test/web/servlet/MockMvc.html) | [ResultActions](https://docs.spring.io/spring/docs/4.3.7.RELEASE/javadoc-api/org/springframework/test/web/servlet/ResultActions.html) | [MockMvcResultMatchers](https://docs.spring.io/spring/docs/4.3.7.RELEASE/javadoc-api/org/springframework/test/web/servlet/result/MockMvcResultMatchers.html)