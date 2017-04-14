package com.mengyunzhi.service;

import com.mengyunzhi.repository.Klass;

/**
 * Created by panjie on 17/4/7.
 */
public interface KlassService {
    Klass save(String name, Long teacherId);
    Klass get(Long id);
    void delete(Long id);
    Klass update(Long id, String name, Long teacherId);
    Klass getNewKlass();    // 获取一个新的教师实例
}
