package com.mengyunzhi.repository;

import com.mengyunzhi.service.KlassService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.*;
/**
 * Created by panjie on 17/4/25.
 */
@RunWith(SpringRunner.class) @SpringBootTest
public class CourseRepositoryTest {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired private KlassService klassService;

    @Test
    public void test() {
        // 无异常，则说明测试通过
        courseRepository.findOne(1L);
    }

    @Test
    @Transactional
    public void manyToMany() {
        // 创建两个班级
        Klass klass1 = klassService.getNewKlass();
        Klass klass2 = klassService.getNewKlass();

        // 创建一个课程
        Course course = new Course();
        course.setName("测试课程");

        // 设置这个课程对应刚刚创建的两个班级
        HashSet<Klass> klasses = new HashSet<Klass>();
        klasses.add(klass1);
        klasses.add(klass2);
        course.setKlass(klasses);

        // 保存数据
        courseRepository.save(course);

        // 查询并断言
        course = courseRepository.findOne(course.getId());
        // 断言能查到这个实体
        assertThat(course).isNotNull();
        // 断言这个实体上对应两个班级实体
        assertThat(course.getKlass().size()).isEqualTo(2);
    }
}