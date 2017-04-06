package com.mengyunzhi.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by panjie on 17/4/6.
 */
// 使用以下两个注解来说明：本测试类基于SpringBoot。(必须)
@RunWith(SpringRunner.class)
@SpringBootTest
public class TeacherRepositoryTest {
    // @Autowired注解：自动加载Spring为我们自动实例化的实现了TeacherRepository接口的对象
    @Autowired
    private TeacherRepository teacherRepository;

    // @Before注解：在执行单元测试方法 前 执行。
    @Before
    public void before() {
        teacherRepository.save(new Teacher(
                "zhangsan",
                "zhangsan@yunzhiclub.com",
                "scse of hebut",
                false));
        teacherRepository.save(new Teacher(
                "lisi",
                "lisi@yunzhiclub.com",
                "scse of hebut",
                true));
    }

    @Test
    public void findTeacher() {
        Teacher zhangsan = teacherRepository.findOne(1L);
        Teacher lisi = teacherRepository.findOne(2L);
        System.out.println(zhangsan);
        System.out.println(lisi);
    }

    // @Test：本方法为一个单元测试方法
    @Test
    public void addTeacher() {
        // 执行数据保存操作
        System.out.println(teacherRepository.save(new Teacher()));

        // 打印Spring为我们自动实例化的对象
        System.out.println(teacherRepository);
    }
}