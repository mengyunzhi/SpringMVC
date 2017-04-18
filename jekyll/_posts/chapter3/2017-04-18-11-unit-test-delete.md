---
layout: post
title:  "第十一节：单元测试 -- controller -- delete"
description: "本节中，我们对`delete`进行了测试，在测试过程中控制台中报出了异常。就此，我们就验证类的**异常**与程序执行中发生的**异常**做了对比。并由此修改了`delete`方法，在该方法中，我们手动抛出了一个带有异常的`ResponseEntity`来模拟验证失败时发送异常的情景。"
date: 2017-04-18T10:38:46+08:00
categories: chapter3
author: "潘杰"
---

## 时序图

![{{site.imageurl}}/chapter3/16.png]({{site.imageurl}}/chapter3/16.png)

## 初始化
{% highlight java %}
    @Test
    public void delete() throws Exception {
        // 创建班级

        // 删除班级

        // 断言删除成功

        // 删除一个不存在的班级

        // 断言发生错误
    }
{% endhighlight %}

## 正常删除
{% highlight java %}
    @Test
    // 改名为deleteTest，避免与测试中的delete()方法冲突
    public void deleteTest() throws Exception {
        // 创建班级
        Klass klass = klassService.getNewKlass();

        // 删除班级
        this.mockMvc.perform(delete("/klass/" + klass.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk());

        // 断言删除成功
        Klass newKlass = klassService.get(klass.getId());
        assertThat(newKlass).isNull();
{% endhighlight %}

## 异常删除
{% highlight java %}
        // 删除一个不存在的班级
        // 断言发生错误
        this.mockMvc.perform(delete("/klass/3000"))
                .andDo(print())
                .andExpect(status().isBadRequest());
{% endhighlight %}

测试:

![{{site.imageurl}}/chapter3/45.png]({{site.imageurl}}/chapter3/45.png)

{% highlight text %}
org.springframework.web.util.NestedServletException: Request processing failed; nested exception is org.springframework.dao.EmptyResultDataAccessException: No class com.mengyunzhi.repository.Klass entity with id 3000 exists!
{% endhighlight %}

我们本来是想得到一个请求的错误，这样以便使用`andExpect`来获取这个错误。却不想在控制台得到了一个异常。此时，我们回想下在前面进行其它的测试时，比如说更新姓名为空，是可以得到一个请求错误的400异常的。而非系统的异常。这是什么原因导致的呢？

这是由于在前面更新姓名为空时，我们使用的是`@Valid`注解，这验证未通过时，将自动的向请求者抛出一个验证异常（该异常采用设置header中的status值来实现）。
而在此，我们删除一个不存在的实体时，得到是一个`java`内部异常。执行到此时，程序由于异常而中断了。

解决这个测试报异常的方法有两种：

第一种是以前我们学过的，即为这个测试增加一个期待异常的注解。

第二种是在触发器中，去获取这个异常。然后手动对异常进行处理。`Spring`的异常类型很多，处理异常的方法也不止一种。官方也给我们处理异常的相关教程 [Exception Handling in Spring MVC](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc)


第一种：
{% highlight java %}
    @Test(expected = NestedServletException.class)
    // 改名为deleteTest，避免与测试中的delete()方法冲突
    public void deleteTest() throws Exception {
{% endhighlight %}

第二种代码改动也不大，做为选学内容，如果你感觉自己并不能够很快的看明白，这仅是由于我们接触`Spring`接触`java`的时间过短而已。没关系，现在的我们的，直接跳过就可以了。
{% highlight java %}
    @DeleteMapping("/{id}")
    public ResponseEntity<Klass> delete(@PathVariable Long id) {
        try {
            klassService.delete(id);
        } catch (Exception e) {
            return new ResponseEntity<Klass>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Klass>(new HttpHeaders(), HttpStatus.OK);
    }
{% endhighlight %}

无论哪种方式，最后我们都会得到一个测试通过的结果。

<hr />
最后，我们对启动测试类来查看是否通过了全部的测试：

![{{site.imageurl}}/chapter3/45.png]({{site.imageurl}}/chapter3/45.png)


# 总结
本节中，我们对`delete`进行了测试，在测试过程中控制台中报出了异常。就此，我们就验证类的**异常**与程序执行中发生的**异常**做了对比。并由此修改了`delete`方法，在该方法中，我们手动抛出了一个带有异常的`ResponseEntity`来模拟验证失败时发送异常的情景。



参考: | [Handling exceptions](http://docs.spring.io/spring/docs/4.3.7.RELEASE/spring-framework-reference/htmlsingle/#mvc-exceptionhandlers) | [ResponseEntity](http://docs.spring.io/spring/docs/4.3.7.RELEASE/javadoc-api/org/springframework/http/ResponseEntity.html) | [HttpHeaders](http://docs.spring.io/spring/docs/4.3.7.RELEASE/javadoc-api/org/springframework/http/HttpHeaders.html) | [Interface MultiValueMap<K,V>](http://docs.spring.io/spring/docs/4.3.7.RELEASE/javadoc-api/org/springframework/util/MultiValueMap.html) | [HttpStatus](http://docs.spring.io/spring/docs/4.3.7.RELEASE/javadoc-api/org/springframework/http/HttpStatus.html)