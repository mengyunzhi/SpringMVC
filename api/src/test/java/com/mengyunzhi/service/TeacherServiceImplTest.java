package com.mengyunzhi.service;

import com.mengyunzhi.repository.Teacher;
import com.mengyunzhi.repository.TeacherRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
// import static :导入的类为静态类，在本测试用例中只导入一次
import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    public void deleteTeacherTest() {
        // 实例化教师 李四
        Teacher teacherLisi = new Teacher("lisi",
                "lisi@email.com",
                "scse of hebut",
                false);

        // 保存李四至数据库
        teacherLisi = teacherRepository.save(teacherLisi);
        System.out.println(teacherLisi);

        // 执行删除操作
        teacherService.deleteTeacher(teacherLisi);

        // 数据查找，看数据是否进行了删除
        Teacher teacher = teacherRepository.findOne(teacherLisi.getId());
        System.out.println(teacher);
    }

    @Test
    public void deleteTeacherByIdTest() {
        // 实例化教师 李四
        Teacher teacherLisi = new Teacher("lisi",
                "lisi@email.com",
                "scse of hebut",
                false);

        // 保存李四至数据库
        teacherLisi = teacherRepository.save(teacherLisi);
        System.out.println(teacherLisi);

        // 删除关键字对应的实体
        teacherService.deleteTeacherById(teacherLisi.getId());

        // 数据查找，看数据是否进行了删除
        Teacher teacher = teacherRepository.findOne(teacherLisi.getId());
        System.out.println(teacher);

    }

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
        assertThat(teacherLisi.getId()).isNull();
        // 断方李的ID大于0
        assertThat(teacherLisi.getId()).isNotZero();

        // 删除关键字对应的实体
        teacherService.deleteTeacherById(teacherLisi.getId());

        // 数据查找，看数据是否进行了删除
        Teacher teacher = teacherRepository.findOne(teacherLisi.getId());
        // 断言查询到的实体结果为null
        assertThat(teacher).isNull();
    }
}
