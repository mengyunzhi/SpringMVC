---
layout: post
title:  "第十一节：完善测试"
description: "引用断言，进行单元测试"
date: 2017-04-12T15:12:37+08:00
categories: chapter2
author: "潘杰"
---
我们在前面进行单元测试时，启动测试程序后，需要查看控制台来提到是否达到了我们的预期。这在一些基本的`CRUD`操作中，还可以实现。但如果实现的逻辑过多，再使用这种办法，便会捉襟见肘了。`SpringMVC`为我们提供了更加强大的测试方法。

本节中，让我们共同学习使用`SpringMVC`的`test`依赖来进行单元测试。

> 官方教程: | [https://spring.io/guides/gs/testing-web/](https://spring.io/guides/gs/testing-web/)

## 断言测试
{% highlight java %}
// import static :导入的类为静态类，在本测试用例中只导入一次
import static org.assertj.core.api.Assertions.assertThat;

    @Test
    public void unitDeleteTest() {
        // 实例化教师 李四
        Teacher teacherLisi = new Teacher("lisi",
                "lisi@email.com",
                "scse of hebut",
                false);

        // 断言新增教程的ID为null
        assertThat(teacherLisi.getId()).isNull();

        // 保存李四至数据库
        teacherLisi = teacherRepository.save(teacherLisi);

        // 断言李四的id不是null
        assertThat(teacherLisi.getId()).isNotNull();
        // 断方李的ID大于0
        assertThat(teacherLisi.getId()).isNotZero();

        // 删除关键字对应的实体
        teacherService.deleteTeacherById(teacherLisi.getId());

        // 数据查找，看数据是否进行了删除
        Teacher teacher = teacherRepository.findOne(teacherLisi.getId());
        // 断言查询到的实体结果为null
        assertThat(teacher).isNull();
    }
{% endhighlight %}

如上所示，我们导入了`org.assertj.core.api.Assertions.assertThat`静态类，并使用了该类的：`isNull()`来断言值为`null`，`isNotNull()`来断言值不为`null`, `inNotZero()`来断言值不为`0`。

此时，当我们再次启动单元测试时。只需要观察最后的测试结果就可以了。如果测试通过，测试的结果将显示绿色，如果测试不通过，测试的结果将显示红色。此外，我还还可以启动一个测试类中的所有测试用例，最终通过观察测试结果的方法来判断这个类中的所有的测试方法是否通过。

比如我们在测试类中，有5个测试方法，则可以通过对类的测试，一次性的完成5个测试用例:

![class unit test]({{site.imageurl}}/chapter2/35.png)

此时，如果我们随意修改一个断言值，制造一些不成的用例，那么，将得到如下的测试失败的提示:

![class unit test failed]({{site.imageurl}}/chapter2/36.png)

控制台会提示我们共进行了5个测试，其中有一个测试失败了，这个测试是`unitDeleteTest`。当我们修改代码后，我们可以点击最左侧的第二个按钮（图中红圈）来单独启动这个错误的用例。

> 参考文档：| [https://joel-costigliola.github.io/assertj/](https://joel-costigliola.github.io/assertj/) | [http://joel-costigliola.github.io/assertj/core-8/api/org/assertj/core/api/AbstractCharSequenceAssert.html#containsOnlyDigits--](http://joel-costigliola.github.io/assertj/core-8/api/org/assertj/core/api/AbstractCharSequenceAssert.html#containsOnlyDigits--)

# 实际开发
在实际的开发中，流程往往是这样的：
*   高级工程师根据项目经理的要求以及项目原型、需求说明，研发类图、时序图。
*   中级工程师会按类图与时序图进行基础代码的编写。
    *   基本代码的编写过程中，我们只给出函数名，参数，返回值直接为null。
    *   中级工程师们针对每一个方法，都去建立一个单元测试的方法，在这个方法中，写清楚单元测试的内容。
*   初级工程师们按照类图，时序图，以及单元测试，进行代码的开发。
    *   以是否通过单元测试，做为方法是否开发完毕的依据。


所以，如果我们根本没有接触过单元测试，或是认为单元测试繁琐，完全没有必要的话。那么，我们的定位只能是程序员(computer programmer)，而非软件工程师（software development engineer）。
下图诠释了两者的区别:

![cs sde]({{site.imageurl}}/chapter2/37.png)

让我们一起，在计算机工程中，不倦地追求高远，彻底脱离cs队伍，投入software development engineer的怀抱。Just do it!


