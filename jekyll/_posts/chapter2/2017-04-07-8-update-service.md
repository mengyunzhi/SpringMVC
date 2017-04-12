---
layout: post
title:  "第八节：更新数据 -- service"
description: "正式引用service进行数据的逻辑处理"
date: 2017-04-07T15:20:42+08:00
categories: chapter2
author: "潘杰"
---

在`SpringMVC`中，`C`层更多在功能是：数据转发，数据验证，数据绑定，路由设定等。而不负责具体的数据的处理。显然的，我们上述代码的C层中，对数据进行逻辑处理。这违背了上述的原则。`SpringMVC`中，`service`层负责对进行逻辑运算及数据的处理。

我们在使用`teacherRepository`时，已经体验了`spring`面向接口编程的魅力。和前面一样，在建立`service`时，同样也是面向接口的。我们在其实类中引用的，也是接口，而非对象。

## 建立接口
建立文件
```
package com.mengyunzhi.service;

/**
 * Created by panjie on 17/4/7.
 */
public interface TeacherService {
}

```

<hr />
建立方法
```
package com.mengyunzhi.service;

import com.mengyunzhi.repository.Teacher;

/**
 * Created by panjie on 17/4/7.
 */
public interface TeacherService {
    /**
     * 保存
     * @param id 关键字
     * @param teacher 教师
     * @return 保存后的教师
     */
    Teacher saveTeacher(Long id, Teacher teacher);
}
```
<hr />
声明抛出异常类型
```
...
import javax.persistence.EntityNotFoundException;
    ...
    Teacher saveTeacher(Long id, Teacher teacher) throws EntityNotFoundException;
    ...
```

## 实现接口
![imp interface]({{site.imageurl}}/chapter2/2.gif)

没错，有了`IDEA`一切操作就是如此的简单。如果再结合快捷键的话，相信还将是另一番天地。

<hr />
加入`Service`注解
```
@Service            // 说明本类是一个Service，Spring在进行自动注入的时候，会将有此类注入到相应的TeacherService中。
public class TeacherServiceImpl implements TeacherService {
```

<hr />
自动注入`TeacherRepository`
```
    @Autowired
    private TeacherRepository teacherRepository;
```

<hr />
实现数据更新
```
    @Override
    public Teacher saveTeacher(Long id, Teacher teacher) throws EntityNotFoundException {
        // 判断是否存在该实体，如果不存在，则报错
        if (teacherRepository.findOne(id) == null) {
            throw new EntityNotFoundException("传入的ID值：" + id.toString() + "有误。未找到对应的实体");
        }

        // 对实体ID赋值, 并执行更新操作
        teacher.setId(id);
        return teacherRepository.save(teacher);
    }
```

## 单元测试
在`test`文件夹中，新建对应的测试类，进行单元测试。

![new unit test]({{site.imageurl}}/chapter2/26.png)

增加单元测试注解
```
@RunWith(SpringRunner.class)
@SpringBootTest
public class TeacherServiceImplTest {
```
<hr />
编写测试方法:
```
    @Test
    public void saveTeacherTest() {
        // 实例化教师 李四
       
        // 保存李四至数据库
        
        // 打印李四的数据

        // 实例化教师 张三
       
        // 将张三的数据更新到原李四的数据中

        // 查询并打印更新后的数据
        
    }
```

<hr />
代码实现：
```
    @Test
    public void saveTeacherTest() {
        // 实例化教师 李四
        Teacher teacherLisi = new Teacher("lisi",
                "lisi@email.com",
                "scse of hebut",
                false);

        // 保存李四至数据库
        teacherLisi = teacherRepository.save(teacherLisi);

        // 打印李四的数据
        System.out.println(teacherLisi);

        // 实例化教师 张三
        Teacher teacherZhangsan = new Teacher(
                "zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                true);

        // 将张三的数据更新到原李四的数据中
        Teacher newTeacher = teacherService.saveTeacher(teacherLisi.getId(), teacherZhangsan);

        // 查询并打印更新后的数据
        System.out.println(newTeacher);
    }
```

> 在代码实现中，我们使用到了`teacherRepository`及`teacherService`, 这两个对象也是通过`@Autowired`来由`Spring`完成的。

完整代码如下：
```
package com.mengyunzhi.service;

import com.mengyunzhi.repository.Teacher;
import com.mengyunzhi.repository.TeacherRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by panjie on 17/4/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TeacherServiceImplTest {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void saveTeacherTest() {
        // 实例化教师 李四
        Teacher teacherLisi = new Teacher("lisi",
                "lisi@email.com",
                "scse of hebut",
                false);

        // 保存李四至数据库
        teacherLisi = teacherRepository.save(teacherLisi);

        // 打印李四的数据
        System.out.println(teacherLisi);

        // 实例化教师 张三
        Teacher teacherZhangsan = new Teacher(
                "zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                true);

        // 将张三的数据更新到原李四的数据中
        Teacher newTeacher = teacherService.saveTeacher(teacherLisi.getId(), teacherZhangsan);

        // 查询并打印更新后的数据
        System.out.println(newTeacher);
    }
}
```

我们点击方法前面的绿色启动按钮，来启动单元测试，最终将在控制台得到如下信息：
```
Hibernate: insert into teacher (address, email, name, sex) values (?, ?, ?, ?)
Teacher{id=1, name='lisi', email='lisi@email.com', address='scse of hebut', sex=false}
Hibernate: select teacher0_.id as id1_0_0_, teacher0_.address as address2_0_0_, teacher0_.email as email3_0_0_, teacher0_.name as name4_0_0_, teacher0_.sex as sex5_0_0_ from teacher teacher0_ where teacher0_.id=?
Hibernate: select teacher0_.id as id1_0_0_, teacher0_.address as address2_0_0_, teacher0_.email as email3_0_0_, teacher0_.name as name4_0_0_, teacher0_.sex as sex5_0_0_ from teacher teacher0_ where teacher0_.id=?
Hibernate: update teacher set address=?, email=?, name=?, sex=? where id=?
Teacher{id=1, name='zhangsan', email='zhangsan@yunzhiclub.com', address='scse of hebut', sex=true}
```
其中，有`Hibernate: `前缀的，为`Hibernate`生成的数据库操作语句。
通过观察，我们不难发现，先后进行数据的插入、更新操作。

## 完善测试
有了正确的用例，我们也需要错误的用例。
新建方法，并加入注释：
```
    /**
     * 更新的数据不存在于数据表中时，发生错误，并抛出异常
     */
    @Test
    public void saveTeacherErrorTest() {
        // 指定id为0

        // 实例化教师张三

        // 使用张三的数据来更新0号教师的数据
    }
```

<hr />
代码实现：
```
    /**
     * 更新的数据不存在于数据表中时，发生错误，并抛出异常
     */
    @Test
    public void saveTeacherErrorTest() {
        // 指定id为0
        Long id = 0L;

        // 实例化教师张三
        Teacher teacherZhangsan = new Teacher(
                "zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                true);

        // 使用张三的数据来更新0号教师的数据
        teacherService.saveTeacher(id, teacherZhangsan);
    }
```

测试用例：
![update error unit test]({{site.imageurl}}/chapter2/27.png)

虽然这样达到了我们测试的目的，但从单元测试的规范上讲，红色代表测试发生异常，绿色才是我们想看到的。为此，我们为`@Test`指定一个参数，进而表明，此测试方法的期待结果是获取一个`EntityNotFoundException`类型的异常。
```
    @Test(expected = EntityNotFoundException.class)
    public void saveTeacherErrorTest() {
```
此时，当我们再次进行测试时，发现控制台没有报告异常，而且测试结果显示为绿色，表示测试通过。
![update error unit test]({{site.imageurl}}/chapter2/28.png)

完整代码如下：
```
package com.mengyunzhi.service;

import com.mengyunzhi.repository.Teacher;
import com.mengyunzhi.repository.TeacherRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;

/**
 * Created by panjie on 17/4/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TeacherServiceImplTest {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void saveTeacherTest() {
        // 实例化教师 李四
        Teacher teacherLisi = new Teacher("lisi",
                "lisi@email.com",
                "scse of hebut",
                false);

        // 保存李四至数据库
        teacherLisi = teacherRepository.save(teacherLisi);

        // 打印李四的数据
        System.out.println(teacherLisi);

        // 实例化教师 张三
        Teacher teacherZhangsan = new Teacher(
                "zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                true);

        // 将张三的数据更新到原李四的数据中
        Teacher newTeacher = teacherService.saveTeacher(teacherLisi.getId(), teacherZhangsan);

        // 查询并打印更新后的数据
        System.out.println(newTeacher);
    }

    /**
     * 更新的数据不存在于数据表中时，发生错误，并抛出异常
     */
    @Test(expected = EntityNotFoundException.class)     // 此方法的预期结果是获取到一个EntityNotFoundException异常
    public void saveTeacherErrorTest() {
        // 指定id为0
        Long id = 0L;

        // 实例化教师张三
        Teacher teacherZhangsan = new Teacher(
                "zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                true);

        // 使用张三的数据来更新0号教师的数据
        teacherService.saveTeacher(id, teacherZhangsan);
    }
}

```

> 单元测试参考文档：[http://wiki.jikexueyuan.com/project/junit/exceptions-test.html](http://wiki.jikexueyuan.com/project/junit/exceptions-test.html)
