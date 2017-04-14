package com.mengyunzhi.service;

import com.mengyunzhi.repository.Teacher;

import javax.persistence.EntityNotFoundException;

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
    Teacher saveTeacher(Long id, Teacher teacher) throws EntityNotFoundException;
    void deleteTeacher(Teacher teacher);    // 删除实体
    void deleteTeacherById(Long id);        // 删除实体
    Teacher getNewTeacher();                // 获取一个新的教师实例
}
