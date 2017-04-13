package com.mengyunzhi.repository;

import javax.persistence.*;

/**
 * Created by panjie on 17/4/13.
 */
@Entity
public class Klass {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;

    @ManyToOne // 多对一关联
    private Teacher teacher;

    private String name;        // 名称

    public Klass() {
    }

    public Klass(Teacher teacher, String name) {
        this.teacher = teacher;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Klass{" +
                "id=" + id +
                ", teacher=" + teacher +
                ", name='" + name + '\'' +
                '}';
    }
}
