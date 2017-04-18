---
layout: post
title:  "第十节：单元测试 -- controller -- update"
description: "我们由时序图出发，获取了update操作的请求数据与响应数据类型与格式，并依据该类型格式及我们的制定的开发规范（更新操作为put,格式为json）,书写了测试用例。同时，在测试中，针对用户可以传入数据错误的问题"
date: 2017-04-18T08:35:06+08:00
categories: chapter3
author: "潘杰"
---
通过前面两节的学习，我们总结出：在进行一个单元测试以前，需要对请求数据与响应数据的内容、格式有着非常清晰正确的认识。

所以，在进行单元测试前，我们首先查看时序图对该触发器是如何进行描述的。

![{{site.imageurl}}/chapter3/17.png]({{site.imageurl}}/chapter3/17.png)

由时序图，我们不难发现请求与响应（返回）数据的类型, 又由于我们规定了请求与响应的数据的格式均为`json`，时此，数据的请求响应类型与格式便全部都确定了。

# 初始化
{% highlight java %}
    @Test
    public void update() throws Exception {
        // 创建一个新班级

        // 更新这个班级的名称

        // 断言返回状态码为200

        // 获取这个班级的名称，并断言其更新成功

        // 创建一个教师

        // 为新教师来更新这个班级的teacherId

        // 获取这个班级的教师信息，并断言其更新成功

        // 更新这个班级的名称为空

        // 断言其会发生400异常

        // 更新这个班级的teacherId为不可能存在的值

        // 断言对应的教师信息为null
        
    }
{% endhighlight %}

> 先有注释，再有代码，你做到了吗？

## 更新班级名称
{% highlight java %}
import static org.assertj.core.api.Assertions.assertThat;
    @Test
    public void update() throws Exception {
        // 创建一个新班级
        Klass klass = klassService.getNewKlass();

        // 更新这个班级的名称
        String klassName = "testName";

        // 断言返回状态码为200
        this.mockMvc.perform(
                put("/klass/" + klass.getId().toString())
                        .contentType("application/json")
                        .content("{\"name\":\"" + klassName + "\",\"teacherId\":" + klass.getTeacher().getId().toString() + "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(status().is(200));

        // 获取这个班级的名称，并断言其更新成功
        Klass newKlass = klassService.get(klass.getId());
        assertThat(klassName).isEqualTo(newKlass.getName());
    }
{% endhighlight %}

`isOk()`与`is(200)`的作用完全相同。
`assertThat(klassName).isEqualTo(newKlass.getName())`断言获得的`name`值与传入的`name`值相同。

## 更新teacherId
{% highlight java %}
    @Autowired
    private TeacherService teacherService;
    ...
        // 创建一个教师
        Teacher teacher = teacherService.getNewTeacher();

        // 为新教师来更新这个班级的teacherId
        this.mockMvc.perform(
                put("/klass/" + klass.getId().toString())
                        .contentType("application/json")
                        .content("{\"name\":\"" + klassName + "\",\"teacherId\":" + teacher.getId().toString() + "}"))
                .andDo(print())
                .andExpect(status().isOk());

        // 获取这个班级的教师信息，并断言其更新成功
        Klass newKlassWithNewTeacher = klassService.get(klass.getId());
        assertThat(teacher.getId()).isEqualTo(newKlassWithNewTeacher.getTeacher().getId());
{% endhighlight %}

## 更新名称为空
{% highlight java %}
        // 更新这个班级的名称为空
        String newKlassName = "";

        // 断言其会发生400异常
        this.mockMvc.perform(
                put("/klass/" + klass.getId().toString())
                        .contentType("application/json")
                        .content("{\"name\":\"" + newKlassName + "\",\"teacherId\":" + teacher.getId().toString() + "}"))
                .andDo(print())
                .andExpect(status().is(400));
{% endhighlight %}


## 更新不存在的实体的teacherId
{% highlight java %}
        // 更新这个班级的teacherId为不可能存在的值
        String teacherId = "3000";
        this.mockMvc.perform(
                put("/klass/" + klass.getId().toString())
                        .contentType("application/json")
                        .content("{\"name\":\"" + klassName + "\",\"teacherId\":" + teacherId + "}"))
                .andDo(print())
                .andExpect(status().isOk());

        // 断言对应的教师信息为null
        Klass newKlassWithNullTeacher = klassService.get(klass.getId());
        assertThat(newKlassWithNullTeacher.getTeacher()).isNull();
{% endhighlight %}

至此，我们测试了更新正常的名称、班级ID，测试也更新非正常的名称、班级id，并且全部获得了预期结果。

# 总结：
我们由时序图出发，获取了`update`操作的请求数据与响应数据类型与格式，并依据该类型格式及我们的制定的开发规范（更新操作为`put`,格式为`json`）,书写了测试用例。同时，在测试中，针对用户可以传入数据错误的问题

参考：| [assertj](http://joel-costigliola.github.io/assertj/)
