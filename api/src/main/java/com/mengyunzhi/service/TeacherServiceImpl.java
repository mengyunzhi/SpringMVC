package com.mengyunzhi.service;

import com.mengyunzhi.repository.Teacher;
import com.mengyunzhi.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

/**
 * Created by panjie on 17/4/7.
 */
@Service            // 说明本类是一个Service，Spring在进行自动注入的时候，会将有此类注入到相应的TeacherService中。
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

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
}
