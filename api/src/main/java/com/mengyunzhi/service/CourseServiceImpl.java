package com.mengyunzhi.service;

import com.mengyunzhi.repository.Course;
import com.mengyunzhi.repository.CourseRepository;
import com.mengyunzhi.repository.Klass;
import com.mengyunzhi.repository.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

/**
 * Created by panjie on 17/4/26.
 */
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private KlassService klassService;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    @Transactional
    public void transactional() {
        // 创建两个班级
        Klass klass1 = klassService.getNewKlass();
        Klass klass2 = klassService.getNewKlass();

        Teacher teacher = new Teacher();

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
        return;
    }
}
