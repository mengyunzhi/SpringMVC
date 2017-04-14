---
layout: post
title:  "第七节：代码开发 -- controller"
description: "进行单元测试下的代码开发"
date: 2017-04-14T13:48:51+08:00
categories: chapter3
author: "潘杰"
---
# save
我们删除上节的测试代码，正式开始进行控制器中各触发器的编写。

![{{site.imageurl}}/chapter3/15.png]({{site.imageurl}}/chapter3/15.png)

{% highlight java %}
package com.mengyunzhi.controller;

import com.mengyunzhi.service.KlassService;
import com.mengyunzhi.repository.Klass;
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

    @PostMapping("/")
    public Klass save(@RequestBody JsonInput jsonInput) {
        return klassService.save(jsonInput.getName(), jsonInput.getTeacherId());
    }

    private static class JsonInput {
        private String name;
        private Long TeacherId;
    }
}
{% endhighlight %}

添加构造函数及`set\get`方法:
{% highlight java %}
    private static class JsonInput {
        private String name;
        private Long TeacherId;

        public JsonInput() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getTeacherId() {
            return TeacherId;
        }

        public void setTeacherId(Long teacherId) {
            TeacherId = teacherId;
        }
    }
{% endhighlight %}

启动应用并测试：

![{{site.imageurl}}/chapter3/30.png]({{site.imageurl}}/chapter3/30.png)

查看返回值，发现`teacher`的值为`null`，是的，这是由于我们启动程序时，`Spring`将自动把表清空。所以此时，并没有`teacherId`为1的实体，`Spring`在进行班级实体保存时，发现了这一点，进而用`null`来替代了这个教师实体。

> 思索：如果避免在测试时`teacher`返回`null`呢?

## 增加验证
我们对用户传入的班级名称进行验证，不能为空，最大长度为10.
加入验证注解：
{% highlight java %}
import org.hibernate.validator.constraints.NotEmpty;
        ...
        @NotEmpty
        @Size(max = 10)
        private String name;
{% endhighlight %}

然后，我们在请求的注解中，开启验证
{% highlight java %}
public Klass save(@Valid @RequestBody JsonInput jsonInput) {
{% endhighlight %}

启动应用并测试：
输入`name`为空时:

![{{site.imageurl}}/chapter3/31.png]({{site.imageurl}}/chapter3/31.png)

输入`name`过长时:

![{{site.imageurl}}/chapter3/33.png]({{site.imageurl}}/chapter3/33.png)


### 小结
我们不仅可以在实体中使用验证，还可以在其它类中使用验证。我们不仅可以将数据直接绑定到实体上，还可以将数据绑定至其它的对象上。


# get
![{{site.imageurl}}/chapter3/14.png]({{site.imageurl}}/chapter3/14.png)

{% highlight java %}
    @GetMapping("/{id}")
    public Klass get(@PathVariable Long id) {
        return klassService.get(id);
    }
{% endhighlight %}

测试略。

# update
![{{site.imageurl}}/chapter3/17.png]({{site.imageurl}}/chapter3/17.png)

{% highlight java %}
    @PutMapping("/{id}")
    public Klass update(@PathVariable Long id, @Valid @RequestBody JsonInput jsonInput) {
        return klassService.update(id, jsonInput.getName(), jsonInput.getTeacherId());
    }
{% endhighlight %}

# delete
{% highlight java %}
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        klassService.delete(id);
        return;
    }
{% endhighlight %}
测试略。

由于每启动一次应用，都用清空数据表。所以在上面的测试过程中，相信你也会得到了很多想不到的异常。原因也很简单，比如你在查询数据前，没有增加这个班级。比如你在增加这个班级前没有先增加一个教师；再比如，你在更新数据前没有新增一个班级。总之，我们发现使用`postman`测试的时候，好像也并没有那么简单了。

# 总结
在第二章教师管理中，我们将用户的输入信息，直接绑定到了实体上。本节中，我们将用户的输入信息绑定到了自定义类中，并进行了验证。这是两种不同的数据绑定方式，在实际的开发过程中，我们可以按实际需求进行选择。



