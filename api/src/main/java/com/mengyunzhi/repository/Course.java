package com.mengyunzhi.repository;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by panjie on 17/4/25.
 */
@Entity
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;    // 名称

    @ManyToMany
    private Set<Klass> klass = new HashSet<Klass>();

    public Course(String name, HashSet<Klass> klass) {
        this.name = name;
        this.klass = klass;
    }

    public Course() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Klass> getKlass() {
        return klass;
    }

    public void setKlass(Set<Klass> klass) {
        this.klass = klass;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", klass=" + klass +
                '}';
    }
}