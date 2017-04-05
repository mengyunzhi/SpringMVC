---
layout: post
title:  "第三节：创建数据表"
description: "展示如何使用JAVA代码来创建数据表"
date: 2017-04-05T15:22:52+08:00
categories: chapter2
author: "潘杰"
---

`SpringMVC`中集成了`hibernate`框架，所以在`SrpingMVC`,有关关系型数据库的部分，我们完全可以参考`hibernate`的开发文档。`hibernate`为我们提供了这样一个功能：将带有相关注解的`java`类自动与`数据表`进行关系。从而使我们可以完全的使用`java`代码来定义数据表。这样的做的优点当然很多，对于我们而言，我们再也不需要为了数据表不统一造成的各种莫名`BUG`而烦恼了。

> `JPA`全称`Java Persistence API`.`JPA`通过`JDK 5.0`注解或`XML`描述对象－关系表的映射关系，并将运行期的实体对象持久化到数据库中。[http://baike.baidu.com/item/JPA](http://baike.baidu.com/item/JPA)。

# 新建实体类
我们新建`repository`包，并在该包中，新建`Teacher`类。
![new table]({{site.imageurl}}/chapter2/3.png)

然后：
*   使用`@Entity`来说明该类对应一个数据表,数据表的名字与类名相同。
*   使用`@Id`来说明：此字段是该表的主键。
*   使用`@GeneratedValue(strategy = GenerationType.AUTO)`来说明：该主键的生成策略为自动，对应MySQL的属性为'Auto increment'

> 只所以要将上述3个注解单独拿出来，是因为以为我们要常与它们打交道。每新建一个实体，上述的操作都是必须的。

```
package com.mengyunzhi.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by panjie on 17/4/5.
 */
// 使用@Entity 来说明该类对应一个数据表
@Entity
public class Teacher {
    //    声明主键
    @Id
    //    声明主键生成策略为 自动
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}

```
## 测试
我们再次点击`IDEA`的`Run`按钮，来重新启动项目。项目成功启动后，我们将得到一个`Teacher`表，这个表中，有一个字段`id`。该字段属性如下：
![edit table]({{site.imageurl}}/chapter2/4.png)

# 增加其它属性
参考`ER`图:

![Logical model]({{site.imageurl}}/chapter2/6.png) 
![physical model]({{site.imageurl}}/chapter2/7.png)


定制JAVA代码:
```
package com.mengyunzhi.repository;

import javax.persistence.*;

/**
 * Created by panjie on 17/4/5.
 */
// 使用@Entity 来说明该类对应一个数据表
@Entity
public class Teacher {
    //    声明主键
    @Id
    //    声明主键生成策略为 自动
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 25)    // 声明字段的长度为25
    private String name;    // 姓名
    @Column(length = 50)
    private String email;   // 邮箱
    private String address; // 地址. 不进行@Column声明，则默认长度为255
    private Boolean sex;    // 性别：0，男。1，女.
}
```
然后我们重新启动项目，将得到拥有如下类型的数据表:
![edit table]({{site.imageurl}}/chapter2/5.png)

最后，按照`jpa`的规范，我们增加`set\get`函数，构造函数及`toString`函数。
最终完整代码如下：
```
package com.mengyunzhi.repository;

import javax.persistence.*;

/**
 * Created by panjie on 17/4/5.
 */
// 使用@Entity 来说明该类对应一个数据表
@Entity
public class Teacher {
    //    声明主键
    @Id
    //    声明主键生成策略为 自动
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 25)            // 声明字段的长度为25
    private String name = "";       // 姓名
    @Column(length = 50)
    private String email = "";      // 邮箱
    private String address = "";    // 地址. 不进行@Column声明，则默认长度为255
    private Boolean sex = false;    // 性别：false(0)，男。true(1)，女.

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getSex() {
        return sex;
    }

    public Teacher(String name, String email, String address, Boolean sex) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.sex = sex;
    }

    public Teacher() {
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", sex=" + sex +
                '}';
    }
}
```
最后，我们重新运行项目，以确保未发生拼写错误。


> [官方文档：java 与 数据表类型对照](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#basic-provided)
