---
layout: post
title:  "第二节：添加jpa、mysql模块"
description: "添加jpa用于连接数据库，添加mysql用于指定数据库类型"
date: 2017-04-05T14:47:23+08:00
categories: chapter2
author: "潘杰"
---
如果在项目刚刚生成时，你查看过生成项目的目录信息，不难发现我们在前面选择过一个叫做`web`的Spring核心模块，被写入了根目录下的`pom.xml`中。

即如下代码：
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

这是`SpringMVC`的核心模块。本章中，我们将使用`mysql`做为后台的数据库。此时，我们需要加入`jpa`模块来说明:本项目是需要关系型数据库来支撑的；我们需要加入`mysql`模块，来说明：本项目使用的关系型数据库的类型为`mysql`。

> 在本教程中，我们仍然使用XAMPP中集成的`mysql`。

我们首先启动`xampp`中的`mysql`服务，并使用`navicat`来建立本项目要使用的数据库: `springmvc`。

![new database springmvc]({{site.imageurl}}/chapter2/1.png)

# 加入JPA模块
我们打开`/pom.xml`文件的如下代码段:
```
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

增加`JPA`模块后：
```
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

重新导入依赖模块:
![new database springmvc]({{site.imageurl}}/chapter2/2.png)

在导入过程中,`IDEA`最下侧将有导入进度的提示。未报错，则导入成功。

**[注意]** 当`pom.xml`第一次变化时，`idea`在右下角会弹出重新导入依赖模块的提示，我们可以进行点击导入，也可以点击提示中的"auto import",这样在下次`pom.xml`变化时，将自动触发`maven`的重新导入命令.

如果你的`IDEA`最没有导入成功，那么我们需要启动控制台，并将目录切换至`pom.xml`文件所在的目录，执行`maven compile`以使`maven`来完成依赖包的导入工作[https://spring.io/guides/gs/maven/](https://spring.io/guides/gs/maven/)。

> 参考官方文档: [https://spring.io/guides/gs/accessing-data-jpa/](https://spring.io/guides/gs/accessing-data-jpa/)

# 加入mysql模块
和`jpa`的步骤相似，我们用同样的方法，加入`mysql`模块，并重新导入该模块。
```
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--jpa 模块：关系型数据库-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <!--数据库类型：mysql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

重新导入依赖模块略。

# 测试
我们点击右上角的运行按钮：
![run project]({{site.imageurl}}/chapter1/9.png)并查看`IDEA`下侧的控制台，将得到如下的错误提示。
```
***************************
APPLICATION FAILED TO START
***************************

Description:

Cannot determine embedded database driver class for database type NONE
```
这是`jpa`模块给我们的一个提示，它在说，虽然我已经启动了，但是你没有告诉我要连接的数据库类型是什么。当然了，除了要告诉它要连接的数据库类型(mysql)以外，我们还需要告诉它要连接的数据名称是什么，对应的用户名和密码又都是什么。

## 配置JPA
打开，`/src/main/resources/application.properties`

配置以下信息：
```
# 在项目初始化时，重新创建数据表
spring.jpa.hibernate.ddl-auto=create
# 指定连接的类型为mysql 连接的地址为：localhost 端口为3306 ，数据为springmvc
spring.datasource.url=jdbc:mysql://localhost:3306/springmvc
# 用户名为root
spring.datasource.username=root
# 密码为空
spring.datasource.password=
```

![new database springmvc]({{site.imageurl}}/chapter2/8.png)

此时，我们再次启动项目，在`mysql`服务启动的前提下，将得到启动成功的提示:
```
2017-04-05 15:16:17.776  INFO 4627 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
2017-04-05 15:16:17.782  INFO 4627 --- [           main] com.mengyunzhi.SpringMvcApplication      : Started SpringMvcApplication in 6.929 seconds (JVM running for 7.965)
```

> 参考官方文档：[https://spring.io/guides/gs/accessing-data-mysql/](https://spring.io/guides/gs/accessing-data-mysql/)
